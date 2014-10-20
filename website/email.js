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
