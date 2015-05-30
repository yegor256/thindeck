/*globals casper:false */
[
    '/',
    '/xsl/layout.xsl',
    '/css/style.css',
    '/js/main.js',
    '/robots.txt',
    '/acc'
].forEach(
    function (page) {
        casper.test.begin(
            page + ' page can be rendered',
            function (test) {
                casper.start(
                    casper.cli.get("home") + page,
                    function () {
                        test.assertHttpStatus(200, page);
                    }
                );
                casper.run(
                    function () {
                        test.done();
                    }
                );
            }
        );
    }
);
