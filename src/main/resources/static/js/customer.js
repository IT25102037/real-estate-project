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

    fetch("http://localhost:8080/api/customers", {
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

async function fetchListings() {
    const grid = document.getElementById('dynamic-property-grid');

    try {
        // 1. Call your Spring Boot API
        const response = await fetch('http://localhost:8080/api/listings/all');
        const listings = await response.json();

        // 2. Clear existing placeholder content
        grid.innerHTML = '';

        // 3. Loop through the data from MySQL
        listings.forEach(item => {
            const card = document.createElement('div');
            card.className = 'property-card';

            // Format the price nicely
            const priceLabel = new Intl.NumberFormat('en-US', {
                style: 'currency',
                currency: 'USD',
                maximumFractionDigits: 0
            }).format(item.price);

            // 4. Build the card structure
            card.innerHTML = `
                <div class="img-container">
                    <span class="badge">New Listing</span>
                    <img src="${item.imagePath}" alt="${item.propertyTitle}">
                </div>
                <div class="card-content">
                    <p class="location">📍 ${item.location}</p>
                    <h3>${item.propertyTitle}</h3>
                    <p class="price">${priceLabel}</p>
                    <button class="view-btn">View Details</button>
                </div>
            `;

            grid.appendChild(card);
        });
    } catch (error) {
        console.error("Error loading properties:", error);
        grid.innerHTML = `<p>Error loading properties. Please ensure the backend is running.</p>`;
    }
}

// 5. Run the function when the page opens
window.addEventListener('DOMContentLoaded', fetchListings);