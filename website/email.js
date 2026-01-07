//  Copyright (c) 2014-2026, Thindeck.com
//  All rights reserved.
//
//  Redistribution and use in source and binary forms, with or without
//  modification, are permitted provided that the following conditions
//  are met: 1) Redistributions of source code must retain the above
//  copyright notice, this list of conditions and the following
//  disclaimer. 2) Redistributions in binary form must reproduce the above
//  copyright notice, this list of conditions and the following
//  disclaimer in the documentation and/or other materials provided
//  with the distribution. 3) Neither the name of the thindeck.com nor
//  the names of its contributors may be used to endorse or promote
//  products derived from this software without specific prior written
//  permission.
//
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
//  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
//  NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
//  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
//  THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
//  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
//  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
//  STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
//  OF THE POSSIBILITY OF SUCH DAMAGE.

function send_email(from, success, error) {
  $.ajax(
    {
      type: 'POST',
      url: 'https://mandrillapp.com/api/1.0/messages/send.json',
      data: {
        'key': 'GMfq6HmqFFR4HGCVfIu6Zw',
        'message': {
          'from_email': from,
          'to': [
            {
              'email': 'team@thindeck.com',
              'name': 'Thindeck',
              'type': 'to'
            }
          ],
          'text': 'Hi,\n\n'
            + '\n\nI am interested to join thindeck, when it is ready.'
            + '\n\n--\nsent through the form at www.thindeck.com',
          'subject': 'want to join thindeck.com',
          'auto_html': true,
          'important': true
        }
      },
      success: success,
      error: error
    }
  );
}
$(
  function() {
    $('#send-form').submit(function() {
      event.preventDefault();
      $("#signup-container").fadeOut(400, function() {
        $("#wait-icon").fadeIn(400);
        send_email(
          $('#email').val(),
          function () {
            $("#wait-icon").hide();
            $("#signup-container").html($("#signed-up-container-success").html());
            $("#signed-up-container-success").fadeIn(400);
          },
          function () {
            $("#wait-icon").hide();
            $("#signup-container").html($("#signed-up-container-failure").html());
            $("#signed-up-container-failure").fadeIn(400);
          }
        );
      });
    });
  }
);
function scrollTo(element) {
    var pos = $(element).offset();
    $('body').animate({ scrollTop: pos.top });
}
