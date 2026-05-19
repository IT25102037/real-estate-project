document.addEventListener("DOMContentLoaded", function () {

    const paymentForm = document.getElementById("paymentForm");

    if (paymentForm) {
        paymentForm.addEventListener("submit", async function (e) {

            e.preventDefault();

            const paymentData = {
                userId: document.getElementById("userId").value,
                propertyId: document.getElementById("propertyId").value,
                amount: document.getElementById("amount").value,
                paymentMethod: document.getElementById("paymentMethod").value,
                paymentType: document.getElementById("paymentType").value
            };

            try {
                const response = await fetch("/api/payments", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(paymentData)
                });

                if (!response.ok) {
                    throw new Error("Payment failed");
                }

                const data = await response.json();

                document.getElementById("message").innerText =
                    "Payment Success. Transaction ID: " + data.transactionId;

                paymentForm.reset();

            } catch (error) {
                console.log(error);

                document.getElementById("message").innerText =
                    "Payment Failed";
            }
        });
    }
});