document.addEventListener("DOMContentLoaded", function () {

    const paymentForm = document.getElementById("paymentForm");
    const propertyDropdown = document.getElementById("propertyId");
    const bankAccountDropdown = document.getElementById("bankAccountId");

    // Fetch properties
    fetch('/api/properties')
        .then(response => {
            if (!response.ok) throw new Error("Failed to load properties");
            return response.json();
        })
        .then(data => {
            data.forEach(property => {
                const option = document.createElement("option");
                option.value = property.id;
                option.textContent = `${property.title || 'Property'} - ${property.location || ''} - $${property.price || 0}`;
                propertyDropdown.appendChild(option);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Error loading properties");
        });

    // Fetch bank accounts
    fetch('/api/bank-accounts')
        .then(response => {
            if (!response.ok) throw new Error("Failed to load bank accounts");
            return response.json();
        })
        .then(data => {
            data.forEach(account => {
                const option = document.createElement("option");
                option.value = account.id;
                option.textContent = `${account.bankName} - ${account.accountHolderName}`;
                bankAccountDropdown.appendChild(option);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Error loading bank accounts");
        });

    if (paymentForm) {
        paymentForm.addEventListener("submit", async function (e) {
            e.preventDefault();

            const paymentData = {
                userId: document.getElementById("userId").value,
                propertyId: document.getElementById("propertyId").value,
                bankAccountId: document.getElementById("bankAccountId").value,
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
                alert("Payment Success!");
                paymentForm.reset();

            } catch (error) {
                console.log(error);
                document.getElementById("message").innerText =
                    "Payment Failed";
            }
        });
    }
});