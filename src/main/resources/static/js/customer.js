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
        password: document.getElementById("password").value
    };

    fetch("/api/customers/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(customerData)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || "Server error " + response.status);
            });
        }
        return response.text(); // or .json()
    })
    .then(data => {
        alert("✨ Welcome to the Elite!\nYour profile has been saved successfully.");
        document.getElementById("customerForm").reset();
    })
    .catch(error => {
        console.error("Error:", error);
        alert("Error: " + error.message);
    });

}


