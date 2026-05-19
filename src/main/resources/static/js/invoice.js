async function loadInvoices() {

    const invoiceList = document.getElementById("invoiceList");

    try {
        const response = await fetch("/api/invoices");

        if (!response.ok) {
            throw new Error("Failed to load invoices");
        }

        const invoices = await response.json();

        let html = "";

        if (invoices.length === 0) {
            html = "<p>No invoices found.</p>";
        }

        invoices.forEach(invoice => {
            html += `
                <div class="card">
                    <p><b>Invoice ID:</b> ${invoice.invoiceId}</p>
                    <p><b>Invoice Number:</b> ${invoice.invoiceNumber}</p>
                    <p><b>Total Amount:</b> ${invoice.totalAmount}</p>
                    <p><b>Payment ID:</b> ${invoice.paymentId}</p>
                    <p><b>Issued Date:</b> ${invoice.issuedDate}</p>
                </div>
            `;
        });

        invoiceList.innerHTML = html;

    } catch (error) {
        console.log(error);

        invoiceList.innerHTML =
            "<p style='color:red;'>Invoice loading failed.</p>";
    }
}