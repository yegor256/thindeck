/*globals casper:false */
casper.test.begin(
    'can add repo',
    function (test) {
        casper.start().then(
            function () {
                this.open(
                    casper.cli.get("home"),
                    {
                        method: 'GET',
                        headers: {
                            'Accept': 'text/html'
                        }
                    }
                ).then(
                    function () {
                        test.assertHttpStatus(200);
                    }
                ).then(
                    function () {
                        this.fill(
                            'form',
                            {
                                'name': 'test_repo_name',
                                'uri': 'test_repo_uri'
                            },
                            true
                        );
                        test.assertHttpStatus(200);
                    }
                );
            }
        );
        casper.run(
            function () {
                test.done();
            }
        );
    }
);
