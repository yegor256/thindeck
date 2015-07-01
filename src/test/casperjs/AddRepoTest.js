/*globals casper:false */
casper.test.begin(
    'can add deck',
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
                        this.fill(
                            'form',
                            {
                                'name': 'demo',
                                'uri': 'https://github.com/yegor256/thindeck.git'
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
