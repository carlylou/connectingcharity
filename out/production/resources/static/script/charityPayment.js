paypal.Button.render({

    env: 'sandbox', // Or 'sandbox'

    client: {
        sandbox: 'AZ4rzAJMd3YLKr5-Av9DuqvF0LdsnIN65aRFQpbNF78-jATGOCKDKHQ9Idk0yIXZuX2E7kJ-xqkp3yI1',
        production: 'xxxxxxxxx'
    },

    commit: true, // Show a 'Pay Now' button

    payment: function (data, actions) {
        return actions.payment.create({
            payment: {
                transactions: [
                    {
                        amount: {total: $('#inputDonationAmount').val(), currency: 'GBP'},
                        description: "Donation for the charity " + $('#charityName')
                    }
                ]
            }
        });
    },

    onError: function(err) {
        $("#amountErrorHint").css("display","");
    },


    onAuthorize: function (data, actions) {
        return actions.payment.execute().then(function (payment) {
            $('#donateModal').modal('toggle');
            var paymentDetails = {
                "donorId": $('#userIdSpan').html(),
                "transactionId": payment.id,
                "charityId": $('#charityIdDiv').html(),
                "amount": payment.transactions[0].amount.total
            };

            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/paypal",
                data: JSON.stringify(paymentDetails),
                success: function (data, textStatus, xhr) {
                    if (xhr.status === 200)
                        console.log("Payment logged.");
                    else
                        console.log("Payment not logged.");
                },
                error: function (e) {
                    console.log("Payment not logged.");
                    console.log("ERROR : ", e);
                }
            });
            $('#donateSuccessModal').on('hidden.bs.modal', function (e) {
                location.reload();
            }).modal('toggle');
        });
    }

}, '#paypal-button');
