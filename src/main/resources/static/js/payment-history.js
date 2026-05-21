async function loadPaymentHistory() {

    try {
        const response = await fetch(
            "http://localhost:8080/api/payment-history"
        );

        if (!response.ok) {
            throw new Error("Failed to load payment history");
        }

        const historyList = await response.json();

        let html = "";

        historyList.forEach(history => {
            html += `
                <div class="card">
                    <p><b>ID:</b> ${history.id}</p>
                    <p><b>Payment ID:</b> ${history.paymentId}</p>
                    <p><b>Old Status:</b> ${history.oldStatus}</p>
                    <p><b>New Status:</b> ${history.newStatus}</p>
                    <p><b>Updated At:</b> ${history.updatedAt}</p>
                </div>
            `;
        });

        if (historyList.length === 0) {
            html = "<p>No payment history found.</p>";
        }

        document.getElementById("historyList").innerHTML = html;

    } catch (error) {
        console.log(error);

        document.getElementById("historyList").innerHTML =
            "<p style='color:red;'>Payment history loading failed.</p>";
    }
}