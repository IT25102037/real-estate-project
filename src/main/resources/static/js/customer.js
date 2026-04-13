document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("customerForm");
    if (form) {
        form.addEventListener("submit", saveCustomer);
    }
});

function saveCustomer(event) {
    event.preventDefault();

    const submitBtn = event.target.querySelector("button");
    const originalText = submitBtn.innerText;

    // UI Feedback
    submitBtn.innerText = "Processing...";
    submitBtn.style.opacity = "0.7";
    submitBtn.disabled = true;

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
            alert("✨ Welcome to the Elite!\nYour profile has been saved successfully.");
            document.getElementById("customerForm").reset();
        } else {
            alert("⚠️ Coordination Error: Could not save profile.");
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("⚠️ Connection Failed: Make sure your Spring Boot server is running on port 8081.");
    })
    .finally(() => {
        submitBtn.innerText = originalText;
        submitBtn.style.opacity = "1";
        submitBtn.disabled = false;
    });
}