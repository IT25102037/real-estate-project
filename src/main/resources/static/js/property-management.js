const propertyApp = (() => {
    const state = {
        properties: [],
        activeChip: "all",
        editingId: null
    };

    const endpoints = { base: "/api/properties" };

    const selectors = {
        buy: { grid: "#buyPropertyGrid", search: "#buySearchInput", sort: "#buySortSelect", summary: "#buyResultsSummary", count: "#buyCount", statsLabel: "#buyStatsLabel" },
        rent: { grid: "#rentPropertyGrid", search: "#rentSearchInput", sort: "#rentSortSelect", summary: "#rentResultsSummary", count: "#rentCount", statsLabel: "#rentStatsLabel" },
        sell: {
            form: "#propertyForm",
            inventory: "#inventoryList",
            search: "#inventorySearchInput",
            filter: "#inventoryTypeFilter",
            formTitle: "#formTitle",
            formStatus: "#formStatus",
            submitText: "#submitButtonText",
            cancelEdit: "#cancelEditBtn",
            type: "#propertyType",
            conditional: "#conditionalFieldWrap",
            previewType: "#previewType",
            previewTitle: "#previewTitle",
            previewLocation: "#previewLocation",
            previewPrice: "#previewPrice",
            previewDescription: "#previewDescription",
            previewExtra: "#previewExtra",
            totalCount: "#totalCount",
            rentalCount: "#rentalCount",
            saleCount: "#saleCount"
        }
    };

    function formatCurrency(value) {
        return new Intl.NumberFormat("en-LK", { style: "currency", currency: "LKR", maximumFractionDigits: 0 }).format(Number(value || 0));
    }

    function titleCase(value) {
        return String(value || "").replace(/[-_]/g, " ").replace(/\b\w/g, char => char.toUpperCase());
    }

    function getExtraMeta(property) {
        if (property.type === "house") return `Floors: ${property.numOfFloors ?? 0}`;
        if (property.type === "apartment") return `Floor: ${property.floorNumber ?? 0}`;
        if (property.type === "rental") return `Monthly rent: ${formatCurrency(property.monthlyRent)}`;
        return "Property ready";
    }

    function propertyMatchesChip(property, chip) {
        return chip === "all" ? true : property.type === chip;
    }

    function sortProperties(list, sortValue) {
        const cloned = [...list];
        if (sortValue === "price-asc") return cloned.sort((a, b) => a.price - b.price);
        if (sortValue === "price-desc") return cloned.sort((a, b) => b.price - a.price);
        return cloned.sort((a, b) => (b.id || 0) - (a.id || 0));
    }

    async function loadProperties() {
        const response = await fetch(endpoints.base);
        if (!response.ok) throw new Error("Could not load properties");
        state.properties = await response.json();
        return state.properties;
    }

    function setupReveal() {
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add("visible");
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.12 });
        document.querySelectorAll("[data-reveal]").forEach(section => observer.observe(section));
    }

    function setupHeader() {
        const header = document.getElementById("mainHeader");
        if (!header) return;
        window.addEventListener("scroll", () => {
            if (window.scrollY > 50) {
                header.style.padding = "10px 0";
                header.style.background = "rgba(255, 255, 255, 0.95)";
                header.style.boxShadow = "0 10px 30px rgba(0,0,0,0.1)";
            } else {
                header.style.padding = "15px 0";
                header.style.background = "rgba(255, 255, 255, 0.8)";
                header.style.boxShadow = "none";
            }
        });
    }

    function showToast(message, isError = false) {
        const toast = document.getElementById("toast");
        if (!toast) return;
        toast.textContent = message;
        toast.style.background = isError ? "rgba(127, 29, 29, 0.94)" : "rgba(15, 23, 42, 0.92)";
        toast.classList.add("show");
        window.clearTimeout(showToast.timeoutId);
        showToast.timeoutId = window.setTimeout(() => toast.classList.remove("show"), 2800);
    }

    function renderGrid(pageKey) {
        const page = selectors[pageKey];
        const grid = document.querySelector(page.grid);
        if (!grid) return;

        const search = document.querySelector(page.search)?.value.trim().toLowerCase() || "";
        const sortValue = document.querySelector(page.sort)?.value || "latest";

        const baseList = state.properties.filter(property => pageKey === "buy" ? property.type !== "rental" : property.type === "rental");
        const filtered = sortProperties(
            baseList.filter(property => propertyMatchesChip(property, state.activeChip) && `${property.title} ${property.location} ${property.description}`.toLowerCase().includes(search)),
            sortValue
        );

        const countNode = document.querySelector(page.count);
        if (countNode) countNode.textContent = String(filtered.length);
        const statsLabel = document.querySelector(page.statsLabel);
        if (statsLabel) statsLabel.textContent = filtered.length === 1 ? "property ready" : "properties ready";
        const summary = document.querySelector(page.summary);
        if (summary) summary.textContent = filtered.length ? `Showing ${filtered.length} matching properties` : "No properties match the current filters";

        if (!filtered.length) {
            grid.innerHTML = `<div class="empty-state">No properties found right now. Try another search or add new inventory from the sell portal.</div>`;
            return;
        }

        grid.innerHTML = filtered.map(property => `
            <article class="property-card">
                <div class="property-media">
                    <span class="card-badge">${pageKey === "buy" ? "Buy" : "Rent"} Collection</span>
                    <span class="card-type">${titleCase(property.type)}</span>
                </div>
                <div class="card-body">
                    <p class="card-location">${property.location}</p>
                    <h4 class="card-title">${property.title}</h4>
                    <p class="card-description">${property.description}</p>
                    <div class="card-meta">
                        <span>${getExtraMeta(property)}</span>
                        <span>ID: ${property.id}</span>
                    </div>
                    <div class="card-footer">
                        <span class="card-price">${formatCurrency(property.price)}</span>
                        <div class="card-actions">
                            <a class="mini-btn" href="/sell?edit=${property.id}">Manage</a>
                        </div>
                    </div>
                </div>
            </article>
        `).join("");
    }

    function setupChips(pageKey) {
        const chipContainer = document.querySelector(`[data-chip-group="${pageKey}"]`);
        if (!chipContainer) return;
        chipContainer.addEventListener("click", event => {
            const button = event.target.closest(".chip");
            if (!button) return;
            chipContainer.querySelectorAll(".chip").forEach(chip => chip.classList.remove("active"));
            button.classList.add("active");
            state.activeChip = button.dataset.filter;
            renderGrid(pageKey);
        });
    }

    function setupListingPage(pageKey) {
        const page = selectors[pageKey];
        if (!document.querySelector(page.grid)) return;
        state.activeChip = "all";
        setupChips(pageKey);
        document.querySelector(page.search)?.addEventListener("input", () => renderGrid(pageKey));
        document.querySelector(page.sort)?.addEventListener("change", () => renderGrid(pageKey));
        loadProperties().then(() => renderGrid(pageKey)).catch(error => {
            document.querySelector(page.grid).innerHTML = `<div class="empty-state">${error.message}. Please make sure the backend is running.</div>`;
        });
    }

    function setConditionalField(type, data = {}) {
        const wrap = document.querySelector(selectors.sell.conditional);
        if (!wrap) return;
        let label = "Number of floors";
        let name = "numOfFloors";
        let value = data.numOfFloors ?? "";

        if (type === "apartment") {
            label = "Floor number";
            name = "floorNumber";
            value = data.floorNumber ?? "";
        } else if (type === "rental") {
            label = "Monthly rent";
            name = "monthlyRent";
            value = data.monthlyRent ?? "";
        }

        wrap.innerHTML = `
            <div class="form-field conditional-field">
                <label for="conditionalInput">${label}</label>
                <input id="conditionalInput" name="${name}" type="number" min="0" step="0.01" value="${value}" required>
            </div>
        `;
    }

    function updatePreview() {
        const form = document.querySelector(selectors.sell.form);
        if (!form) return;
        const data = new FormData(form);
        const type = data.get("type");
        const map = {
            [selectors.sell.previewType]: titleCase(type),
            [selectors.sell.previewTitle]: data.get("title") || "Waiting for title",
            [selectors.sell.previewLocation]: data.get("location") || "Waiting for location",
            [selectors.sell.previewPrice]: data.get("price") ? formatCurrency(data.get("price")) : "Waiting for price",
            [selectors.sell.previewDescription]: data.get("description") || "Property description preview will appear here."
        };
        Object.entries(map).forEach(([selector, value]) => {
            const node = document.querySelector(selector);
            if (node) node.textContent = value;
        });

        const extraNode = document.querySelector(selectors.sell.previewExtra);
        const conditionalInput = form.querySelector("#conditionalInput");
        if (extraNode && conditionalInput) {
            const label = conditionalInput.closest(".form-field").querySelector("label").textContent;
            extraNode.textContent = conditionalInput.value ? `${label}: ${conditionalInput.value}` : "Specific property detail will appear here.";
        }
    }

    function payloadFromForm() {
        const data = new FormData(document.querySelector(selectors.sell.form));
        const type = data.get("type");
        const payload = {
            type,
            title: data.get("title"),
            location: data.get("location"),
            price: Number(data.get("price")),
            description: data.get("description")
        };
        if (type === "house") payload.numOfFloors = Number(data.get("numOfFloors"));
        if (type === "apartment") payload.floorNumber = Number(data.get("floorNumber"));
        if (type === "rental") payload.monthlyRent = Number(data.get("monthlyRent"));
        return payload;
    }

    function resetForm(message = "Ready to publish a new property.") {
        const form = document.querySelector(selectors.sell.form);
        if (!form) return;
        form.reset();
        state.editingId = null;
        const type = document.querySelector(selectors.sell.type).value || "house";
        setConditionalField(type);
        updatePreview();
        document.querySelector(selectors.sell.formTitle).textContent = "Create listing";
        document.querySelector(selectors.sell.submitText).textContent = "Save Property";
        document.querySelector(selectors.sell.formStatus).textContent = message;
        document.querySelector(selectors.sell.cancelEdit).hidden = true;
    }

    function fillForm(property) {
        const form = document.querySelector(selectors.sell.form);
        if (!form) return;
        state.editingId = property.id;
        form.elements.type.value = property.type;
        setConditionalField(property.type, property);
        form.elements.title.value = property.title;
        form.elements.location.value = property.location;
        form.elements.price.value = property.price;
        form.elements.description.value = property.description;
        updatePreview();
        document.querySelector(selectors.sell.formTitle).textContent = `Edit property #${property.id}`;
        document.querySelector(selectors.sell.submitText).textContent = "Update Property";
        document.querySelector(selectors.sell.formStatus).textContent = "Edit mode is active. Save to update the database.";
        document.querySelector(selectors.sell.cancelEdit).hidden = false;
        form.scrollIntoView({ behavior: "smooth", block: "start" });
    }

    async function saveProperty(event) {
        event.preventDefault();
        const isEditing = state.editingId !== null;
        const response = await fetch(isEditing ? `${endpoints.base}/${state.editingId}` : endpoints.base, {
            method: isEditing ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payloadFromForm())
        });
        if (!response.ok) throw new Error(isEditing ? "Could not update property" : "Could not save property");
        showToast(isEditing ? "Property updated successfully." : "Property saved successfully.");
        await loadProperties();
        renderInventory();
        resetForm(isEditing ? "Property updated. You can edit another item now." : "New property saved to the database.");
    }

    async function deleteProperty(id) {
        if (!window.confirm("Delete this property from the database?")) return;
        const response = await fetch(`${endpoints.base}/${id}`, { method: "DELETE" });
        if (!response.ok) throw new Error("Could not delete property");
        showToast("Property deleted successfully.");
        await loadProperties();
        renderInventory();
        if (state.editingId === id) resetForm("Deleted the property that was being edited.");
    }

    function renderInventory() {
        const list = document.querySelector(selectors.sell.inventory);
        if (!list) return;
        const search = document.querySelector(selectors.sell.search)?.value.trim().toLowerCase() || "";
        const typeFilter = document.querySelector(selectors.sell.filter)?.value || "all";
        const filtered = state.properties
            .filter(property => typeFilter === "all" ? true : property.type === typeFilter)
            .filter(property => `${property.title} ${property.location} ${property.description}`.toLowerCase().includes(search))
            .sort((a, b) => (b.id || 0) - (a.id || 0));

        document.querySelector(selectors.sell.totalCount).textContent = String(state.properties.length);
        document.querySelector(selectors.sell.rentalCount).textContent = String(state.properties.filter(property => property.type === "rental").length);
        document.querySelector(selectors.sell.saleCount).textContent = String(state.properties.filter(property => property.type !== "rental").length);

        if (!filtered.length) {
            list.innerHTML = `<div class="empty-state">No inventory matches this search yet.</div>`;
            return;
        }

        list.innerHTML = filtered.map(property => `
            <article class="inventory-item">
                <div class="inventory-item-header">
                    <div>
                        <span class="tag">${titleCase(property.type)}</span>
                        <h5>${property.title}</h5>
                    </div>
                    <strong>${formatCurrency(property.price)}</strong>
                </div>
                <p>${property.location}</p>
                <p>${property.description}</p>
                <div class="inventory-item-footer">
                    <span class="helper-text">${getExtraMeta(property)}</span>
                    <div class="card-actions">
                        <button class="mini-btn" type="button" data-action="edit" data-id="${property.id}">Edit</button>
                        <button class="mini-btn danger-btn" type="button" data-action="delete" data-id="${property.id}">Delete</button>
                    </div>
                </div>
            </article>
        `).join("");

        list.querySelectorAll("[data-action='edit']").forEach(button => {
            button.addEventListener("click", () => {
                const property = state.properties.find(item => item.id === Number(button.dataset.id));
                if (property) fillForm(property);
            });
        });

        list.querySelectorAll("[data-action='delete']").forEach(button => {
            button.addEventListener("click", () => deleteProperty(Number(button.dataset.id)).catch(error => showToast(error.message, true)));
        });
    }

    function setupSellPage() {
        const form = document.querySelector(selectors.sell.form);
        if (!form) return;
        const typeSelect = document.querySelector(selectors.sell.type);
        setConditionalField(typeSelect.value);
        updatePreview();
        typeSelect.addEventListener("change", () => {
            setConditionalField(typeSelect.value);
            updatePreview();
        });
        form.addEventListener("input", updatePreview);
        form.addEventListener("submit", event => saveProperty(event).catch(error => showToast(error.message, true)));
        document.querySelector(selectors.sell.cancelEdit).addEventListener("click", () => resetForm());
        document.querySelector(selectors.sell.search).addEventListener("input", renderInventory);
        document.querySelector(selectors.sell.filter).addEventListener("change", renderInventory);

        loadProperties().then(() => {
            renderInventory();
            const editId = Number(new URLSearchParams(window.location.search).get("edit"));
            if (editId) {
                const property = state.properties.find(item => item.id === editId);
                if (property) fillForm(property);
            }
        }).catch(error => {
            document.querySelector(selectors.sell.inventory).innerHTML = `<div class="empty-state">${error.message}. Please make sure the backend is running.</div>`;
            showToast(error.message, true);
        });
    }

    function init() {
        setupHeader();
        setupReveal();
        setupListingPage("buy");
        setupListingPage("rent");
        setupSellPage();
        if (window.lucide) window.lucide.createIcons();
    }

    return { init };
})();

window.addEventListener("DOMContentLoaded", propertyApp.init);

// 1. Updated renderInventory Function
function renderInventory(searchTerm = "") {
    const list = document.querySelector("#inventoryList");
    if (!list) return;

    // Filter for Houses/Apartments and match search input
    const filtered = state.properties
        .filter(property => property.type === "house" || property.type === "apartment")
        .filter(property =>
            property.id.toString().includes(searchTerm) ||
            property.title.toLowerCase().includes(searchTerm) ||
            property.location.toLowerCase().includes(searchTerm)
        )
        .sort((a, b) => (b.id || 0) - (a.id || 0));

    // Update the Sale count in the dashboard header
    const saleCountNode = document.querySelector(selectors.sell.saleCount);
    if (saleCountNode) saleCountNode.textContent = String(filtered.length);

    if (filtered.length === 0) {
        list.innerHTML = `<tr><td colspan="6" style="text-align:center; padding: 50px; opacity: 0.5;">No sale properties found.</td></tr>`;
        return;
    }

    list.innerHTML = filtered.map(property => `
        <tr>
            <td style="color: #c9a96e; font-weight: bold;">#${property.id}</td>
            <td><span class="tag">${property.type}</span></td>
            <td><strong>${property.title}</strong></td>
            <td>${property.location}</td>
            <td style="font-weight: 600;">${formatCurrency(property.price)}</td>
            <td>
                <div class="action-gap">
                    <button class="btn-icon btn-edit-gold" data-action="edit" data-id="${property.id}">
                        <i data-lucide="edit-3" style="width:14px; height:14px;"></i> Edit
                    </button>
                    <button class="btn-icon btn-delete-red" data-action="delete" data-id="${property.id}">
                        <i data-lucide="trash-2" style="width:14px; height:14px;"></i> Delete
                    </button>
                </div>
            </td>
        </tr>
    `).join("");

    if (window.lucide) window.lucide.createIcons();

    // Add event listeners back to the newly created buttons
    list.querySelectorAll("[data-action='edit']").forEach(btn => {
        btn.onclick = () => {
            const prop = state.properties.find(p => p.id === Number(btn.dataset.id));
            if (prop) fillForm(prop);
        };
    });

    list.querySelectorAll("[data-action='delete']").forEach(btn => {
        btn.onclick = () => deleteProperty(Number(btn.dataset.id)).catch(err => showToast(err.message, true));
    });
}

// 2. Add Search Listener (Place inside setupSellPage function)
const searchInput = document.getElementById('inventorySearch');
if (searchInput) {
    searchInput.addEventListener('input', (e) => {
        renderInventory(e.target.value.toLowerCase());
    });
}
