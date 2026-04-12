// Listen for the form submission on the Registration Page
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("customerForm");
    if (form) {
        form.addEventListener("submit", saveCustomer);
    }
});

// Function to save a new customer
function saveCustomer(event) {
    event.preventDefault(); // Prevent page reload

    const customerData = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        address: document.getElementById("address").value,
        propertyType: document.getElementById("propertyType").value
    };

    fetch("http://localhost:8081/api/customers", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(customerData)
    })
    .then(response => {
        if (response.ok) {
            alert("✅ Customer saved successfully!");
            document.getElementById("customerForm").reset();
        } else {
            alert("⚠️ Failed to save customer.");
        }
    })
    .catch(error => console.error("Error:", error));
}