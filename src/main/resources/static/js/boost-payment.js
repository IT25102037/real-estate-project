document.addEventListener("DOMContentLoaded", function () {

    const boostForm = document.getElementById("boostForm");

    if (boostForm) {
        boostForm.addEventListener("submit", async function (e) {

            e.preventDefault();

            const boostData = {
                userId: document.getElementById("userId").value,
                propertyId: document.getElementById("propertyId").value,
                paymentId: document.getElementById("paymentId").value,
                boostPackage: document.getElementById("boostPackage").value,
                boostDays: document.getElementById("boostDays").value
            };

            try {
                const response = await fetch("/api/boost-payments", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(boostData)
                });

                if (!response.ok) {
                    throw new Error("Failed to create boost payment");
                }

                document.getElementById("message").innerText =
                    "Boost payment created successfully";

                boostForm.reset();

            } catch (error) {
                console.log(error);

                document.getElementById("message").innerText =
                    "Boost payment creation failed";
            }
        });
    }
});

async function loadBoostPayments() {

    const boostList = document.getElementById("boostList");

    try {
        const response = await fetch("/api/boost-payments");

        if (!response.ok) {
            throw new Error("Failed to load boost payments");
        }

        const boosts = await response.json();

        let html = "";

        if (boosts.length === 0) {
            html = "<p>No boost payments found.</p>";
        }

        boosts.forEach(boost => {
            html += `
                <div class="card">
                    <p><b>ID:</b> ${boost.id}</p>
                    <p><b>User ID:</b> ${boost.userId}</p>
                    <p><b>Property ID:</b> ${boost.propertyId}</p>
                    <p><b>Payment ID:</b> ${boost.paymentId}</p>
                    <p><b>Package:</b> ${boost.boostPackage}</p>
                    <p><b>Days:</b> ${boost.boostDays}</p>
                    <p><b>Start Date:</b> ${boost.startDate}</p>
                    <p><b>End Date:</b> ${boost.endDate}</p>
                    <p><b>Status:</b> ${boost.status}</p>

                    <button type="button" onclick="expireBoostPayment(${boost.id})">
                        Expire Boost
                    </button>
                </div>
            `;
        });

        boostList.innerHTML = html;

    } catch (error) {
        console.log(error);

        boostList.innerHTML =
            "<p style='color:red;'>Boost payments loading failed.</p>";
    }
}

async function expireBoostPayment(id) {

    try {
        const response = await fetch(`/api/boost-payments/${id}/expire`, {
            method: "PUT"
        });

        if (!response.ok) {
            throw new Error("Failed to expire boost payment");
        }

        alert("Boost expired successfully");

        loadBoostPayments();

    } catch (error) {
        console.log(error);

        alert("Expire boost failed");
    }
}

async function autoExpireBoosts() {

    try {
        const response = await fetch("/api/boost-payments/auto-expire", {
            method: "PUT"
        });

        if (!response.ok) {
            throw new Error("Failed to auto expire boosts");
        }

        alert("Auto expire completed");

        loadBoostPayments();

    } catch (error) {
        console.log(error);

        alert("Auto expire failed");
    }
}