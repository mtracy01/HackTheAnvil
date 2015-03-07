var cheerio = require('cheerio'), http = require('http'), MongoClient = require('mongodb').MongoClient, assert = require('assert'), url = require('url');

function getSoundPage(pageNum, callback) {
	var options = {
		host: 'www.myinstants.com',
		path: '/?page=' + pageNum,
		port: 80
	};
	http.get(options, function(res) {
		assert.equal(res.statusCode, 200);
		var body = '';
		res.on('data', function(p) {
			body += p;
		});
		res.on('end', function() {
			$ = cheerio.load(body);
			sounds = [];
			$('.instant').each(function() {
				var name = $(this).text().trim();
				var url = $(this).find('.small-button').attr('onclick').match(/\'.*\'/)[0];
				url = url.substring(1, url.length - 1)
				url = "www.myinstants.com" + url;
				sounds.push({'name': name, 'url': url});
			});
			callback(pageNum, sounds);
		});
	}).on('error', function(e) {
		assert.fail(e, null);
	});
}

MongoClient.connect('mongodb://localhost:27017/sounds', function(err, db) {
		assert.equal(err, null);
		for(var i = 1; i <= 5; i++) {
			getSoundPage(i, function(pageNum, sounds) {
				var collection = db.collection('page-' + pageNum);
				collection.drop(function(err) {
					assert.equal(err, null);
					collection.insert(sounds, function(err, result) {
						assert.equal(err, null);
					});
				});
			});
		}
		http.createServer(function(req, res) {
			var query = url.parse(req.url, true).query;
			if(query.page) {
				var collection = db.collection('page-' + query.page);
				collection.find({}, {_id: false}).toArray(function(err, items) {
					assert.equal(null, err);
					res.writeHead(200, {'Content-Type': 'application/json'});
					res.end(JSON.stringify(items));
				});

			} else {
				res.writeHead(400, {'Content-Type': 'application/json'});
				res.end('{error: \'bad page number\'}');
			}
		}).listen(1337);
});
