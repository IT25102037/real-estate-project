// Automatically load customers when the page opens
window.onload = function() {
    loadCustomers();
};

// --- READ FUNCTION (Loads table data from API) ---
function loadCustomers() {
    fetch('http://localhost:8081/api/customers')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('customerTableBody');
            tableBody.innerHTML = ''; // Clear existing data

            data.forEach(customer => {
                const row = `<tr>
                    <td>#${customer.id}</td>
                    <td><strong style="color: #1e293b;">${customer.name}</strong></td>
                    <td>
                        <div style="font-size: 13px;">📧 ${customer.email}</div>
                        <div style="font-size: 13px; color: #64748b;">📞 ${customer.phone}</div>
                    </td>
                    <td><span class="property-badge">${customer.propertyType}</span></td>
                    <td style="font-size: 13px;">📍 ${customer.address}</td>
                    <td>
                        <button onclick="deleteCustomer(${customer.id})" class="btn btn-delete">Remove</button>
                    </td>
                </tr>`;
                tableBody.innerHTML += row;
            });

            // Re-apply filter in case something is already in the search box
            filterCustomers();
        })
        .catch(error => console.error('Error loading customers:', error));
}

// --- DELETE FUNCTION ---
function deleteCustomer(id) {
    if(confirm('Are you sure you want to delete Customer ID: ' + id + '?')) {
        fetch(`http://localhost:8081/api/customers/${id}`, {
            method: 'DELETE'
        })
        .then(() => {
            // alert('Customer deleted successfully!');
            loadCustomers(); // Reload the table automatically
        })
        .catch(error => console.error('Error deleting customer:', error));
    }
}

// --- UPDATE FUNCTION (Edit Property Type) ---
function submitUpdate() {
    const customerId = document.getElementById('editCustomerId').value;
    const newProperty = document.getElementById('newPropertyType').value;

    if (!customerId || !newProperty) {
        alert("Please enter both the Customer ID and the New Property Type.");
        return;
    }

    const updatedData = {
        propertyType: newProperty
    };

    fetch(`http://localhost:8081/api/customers/${customerId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to update.");
        }
        return response.json();
    })
    .then(data => {
        alert("Success! Customer ID " + customerId + " property type changed to: " + data.propertyType);

        // Clear the form boxes
        document.getElementById('editCustomerId').value = '';
        document.getElementById('newPropertyType').value = '';

        // Reload the table
        loadCustomers();
    })
    .catch(error => {
        console.error('Error updating:', error);
        alert("Error updating customer. Make sure that ID exists in the table below!");
    });
}

// --- FILTER FUNCTION (Search & Dropdown) ---
function filterCustomers() {
    // 1. Get filter inputs
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const filterProperty = document.getElementById('propertyFilter').value.toLowerCase();

    // 2. Get table body and all rows
    const tableBody = document.getElementById('customerTableBody');
    const rows = tableBody.getElementsByTagName('tr');

    let visibleRowCount = 0;

    // 3. Loop through rows and apply filters
    for (let i = 0; i < rows.length; i++) {
        const row = rows[i];

        // Column indexing (Name is cell[1], email/phone is cell[2], address is cell[4])
        const name = row.cells[1].textContent.toLowerCase();
        const contact = row.cells[2].textContent.toLowerCase(); // grabs both email and phone
        const propertyType = row.cells[3].textContent.toLowerCase();
        const address = row.cells[4].textContent.toLowerCase();

        // Check search against main text columns
        const matchesSearch = name.includes(searchText) || contact.includes(searchText) || address.includes(searchText);

        // Check property dropdown
        const matchesProperty = (filterProperty === "all") || (propertyType.includes(filterProperty));

        // 4. Show/Hide based on BOTH rules
        if (matchesSearch && matchesProperty) {
            row.style.display = ""; // Show
            visibleRowCount++;
        } else {
            row.style.display = "none"; // Hide
        }
    }

    // 5. Handle "No Results" message
    const noResultsMessage = document.getElementById('noResultsMessage');
    if (visibleRowCount === 0 && rows.length > 0) {
        noResultsMessage.style.display = ""; // Show
    } else {
        noResultsMessage.style.display = "none"; // Hide
    }
}