var cheerio = require('cheerio'), http = require('http');

function getSoundPage(pageNum, callback) {
	var options = {
		host: 'www.myinstants.com',
		path: '/?page=' + pageNum,
		port: 80
	};
	http.get(options, function(res) {
		var body = '';
		res.on('data', function(p) {
			body += p;
		});
		res.on('end', function() {
			$ = cheerio.load(body);
			pageSounds = [];
			$('.instant').each(function() {
				var name = $(this).text().trim();
				var url = $(this).find('.small-button').attr('onclick').match(/\'.*\'/)[0];
				url = url.substring(1, url.length - 1)
				url = "www.myinstants.com" + url;
				pageSounds.push({'name': name, 'url': url});
			});
			callback.gotSounds(pageSounds);
		});
	}).on('error', function(e) {
		callback.error(e);
	});
}


getSoundPage(1, {
	gotSounds: function(sounds) { console.log("got sound: ", sounds); },
	error: function(e) { console.log("error", e); }
});
