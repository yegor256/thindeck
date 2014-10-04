/*globals casper:false */
[
    '/',
    '/xsl/layout.xsl',
    '/robots.txt',
    '/acc',
    '/repos',
    '/r/fake-repo',
    '/t/fake-repo/1/log'
].forEach(
    function (page) {
        casper.test.begin(
            page + ' page can be rendered',
            function (test) {
                casper.start(
                    casper.cli.get("home") + page,
                    function () {
                        test.assertHttpStatus(200);
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
