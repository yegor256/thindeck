/*globals casper:false */
/**
 @todo Test is ignored because thindeck is trying to fetch user details from github.
  Find a way to mock that connection, remove "Ignored" from test file name
  and check it works
*/
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
