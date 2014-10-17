/*globals casper:false */
casper.test.begin(
    'can add repo',
    function (test) {
        casper.start(
            casper.cli.get("home") + '/repos',
            function () {
                test.assertHttpStatus(200);
            }
        );
        casper.then(
            function () {
                this.fill('.content > form', {
                    'name' : 'test_repo_name',
                    'uri'  : 'test_repo_uri'
                }, true);
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
