/*globals casper:false */
casper.test.begin(
    'Login can be performed',
    function (test) {
        casper.start(
                casper.cli.get("home")
                + '/?code=xxxd66971baaaffd9610&rexsl-github=',
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
