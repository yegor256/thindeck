require 'sinatra'

set :port, 80

get '/' do
  "<html>
    <body>
      <p>Hello, world!</p>
      <p>Ruby version is #{RUBY_VERSION}</p>
      <p>This page is hosted by <a href='http://www.thindeck.com'>thindeck.com</a>.</p>
      <p>Source code is in <a href='https://github.com/yegor256/thindeck/tree/master/demo-docker-images/ruby-sinatra'>Github</a>.</p>
    </body>
  </html>"
end
