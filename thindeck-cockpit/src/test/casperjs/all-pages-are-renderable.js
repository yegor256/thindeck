/*globals casper:false */
[
    '/stats',
    '/'
].forEach(
    function (page) {
        casper.test.begin(
            page + ' page can be rendered',
            function (test) {
                casper.start(
                    'http://localhost:${tomcat.port}' + page,
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
