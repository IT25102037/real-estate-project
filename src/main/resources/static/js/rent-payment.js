document.addEventListener("DOMContentLoaded", function () {

    const rentForm = document.getElementById("rentForm");

    if (rentForm) {
        rentForm.addEventListener("submit", async function (e) {

            e.preventDefault();

            const rentData = {
                userId: document.getElementById("userId").value,
                propertyId: document.getElementById("propertyId").value,
                rentMonth: document.getElementById("rentMonth").value,
                amount: document.getElementById("amount").value,
                dueDate: document.getElementById("dueDate").value
            };

            try {
                const response = await fetch("/api/rent-payments", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(rentData)
                });

                if (!response.ok) {
                    throw new Error("Failed to create rent payment");
                }

                document.getElementById("message").innerText =
                    "Rent payment created successfully";

                rentForm.reset();

            } catch (error) {
                console.log(error);

                document.getElementById("message").innerText =
                    "Rent payment creation failed";
            }
        });
    }
});

async function loadRentPayments() {

    const rentList = document.getElementById("rentList");

    try {
        const response = await fetch("/api/rent-payments");

        if (!response.ok) {
            throw new Error("Failed to load rent payments");
        }

        const rents = await response.json();

        let html = "";

        if (rents.length === 0) {
            html = "<p>No rent payments found.</p>";
        }

        rents.forEach(rent => {
            html += `
                <div class="card">
                    <p><b>ID:</b> ${rent.id}</p>
                    <p><b>User ID:</b> ${rent.userId}</p>
                    <p><b>Property ID:</b> ${rent.propertyId}</p>
                    <p><b>Rent Month:</b> ${rent.rentMonth}</p>
                    <p><b>Amount:</b> ${rent.amount}</p>
                    <p><b>Status:</b> ${rent.status}</p>
                    <p><b>Due Date:</b> ${rent.dueDate}</p>
                    <p><b>Paid Date:</b> ${rent.paidDate}</p>

                    <button type="button" onclick="markRentAsPaid(${rent.id})">
                        Mark as Paid
                    </button>
                </div>
            `;
        });

        rentList.innerHTML = html;

    } catch (error) {
        console.log(error);

        rentList.innerHTML =
            "<p style='color:red;'>Rent payments loading failed.</p>";
    }
}

async function markRentAsPaid(id) {

    try {
        const response = await fetch(`/api/rent-payments/${id}/mark-paid`, {
            method: "PUT"
        });

        if (!response.ok) {
            throw new Error("Failed to mark rent as paid");
        }

        alert("Rent marked as paid");

        loadRentPayments();

    } catch (error) {
        console.log(error);

        alert("Mark paid failed");
    }
}

async function markOverdueRents() {

    try {
        const response = await fetch("/api/rent-payments/mark-overdue", {
            method: "PUT"
        });

        if (!response.ok) {
            throw new Error("Failed to mark overdue rents");
        }

        alert("Overdue rents updated");

        loadRentPayments();

    } catch (error) {
        console.log(error);

        alert("Mark overdue failed");
    }
}