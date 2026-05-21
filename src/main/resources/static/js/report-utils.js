const API_BASE = window.location.origin;

const REPORT_CATEGORIES = [
    { key: 'DAILY', label: 'Daily Reports' },
    { key: 'WEEKLY', label: 'Weekly Reports' },
    { key: 'MONTHLY', label: 'Monthly Reports' },
    { key: 'TRANSACTION', label: 'Transaction Reports' },
    { key: 'AGENT', label: 'Agent Reports' }
];

function escapeHtml(value) {
    return String(value ?? '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

function formatDate(value) {
    if (!value) return '—';
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return String(value);
    return date.toLocaleString();
}

function parseReportData(report) {
    if (!report || !report.reportData) return null;
    try {
        return JSON.parse(report.reportData);
    } catch (e) {
        return null;
    }
}

function buildReportDocumentHtml(report) {
    const data = parseReportData(report);
    const rows = data ? Object.entries(data).map(([key, val]) => {
        const display = typeof val === 'object' ? JSON.stringify(val, null, 2) : val;
        return `<tr><td style="padding:8px;border:1px solid #ddd;font-weight:600;">${escapeHtml(key)}</td><td style="padding:8px;border:1px solid #ddd;">${escapeHtml(display)}</td></tr>`;
    }).join('') : '';

    return `<!DOCTYPE html><html><head><meta charset="UTF-8"><title>${escapeHtml(report.title)}</title>
    <style>body{font-family:Arial,sans-serif;padding:32px;color:#111;}h1{margin-bottom:8px;}table{border-collapse:collapse;width:100%;margin-top:16px;}</style></head><body>
    <h1>${escapeHtml(report.title)}</h1>
    <p><strong>Type:</strong> ${escapeHtml(report.reportType)}</p>
    <p><strong>Period:</strong> ${escapeHtml(report.startDate || '—')} to ${escapeHtml(report.endDate || '—')}</p>
    <p><strong>Created:</strong> ${formatDate(report.createdAt)}</p>
    ${report.propertyName ? `<p><strong>Subject:</strong> ${escapeHtml(report.propertyName)} · ${escapeHtml(report.clientName || '')}</p>` : ''}
    ${rows ? `<table>${rows}</table>` : '<p>No additional report data.</p>'}
    </body></html>`;
}

function printReport(report) {
    const win = window.open('', '_blank');
    if (!win) return;
    win.document.write(buildReportDocumentHtml(report));
    win.document.close();
    win.focus();
    win.print();
}

function downloadReportPdf(report) {
    const container = document.createElement('div');
    container.style.cssText = 'position:fixed;left:-9999px;top:0;width:800px;padding:24px;background:#fff;color:#111;';
    container.innerHTML = buildReportDocumentHtml(report);
    document.body.appendChild(container);
    const fileName = (report.title || 'report').replace(/[^\w\-]+/g, '_') + '.pdf';
    if (window.html2pdf) {
        html2pdf().set({ margin: 10, filename: fileName, html2canvas: { scale: 2 }, jsPDF: { unit: 'mm', format: 'a4' } })
            .from(container).save().finally(() => container.remove());
    } else {
        printReport(report);
        container.remove();
    }
}

async function readErrorMessage(response, fallback) {
    try {
        const data = await response.json();
        if (data && data.errors) return Object.values(data.errors)[0] || fallback;
        return data.message || data.error || fallback;
    } catch (e) {
        return fallback;
    }
}

async function fetchAllReports() {
    const res = await fetch(`${API_BASE}/api/reports`);
    if (!res.ok) throw new Error('Failed to load reports');
    return res.json();
}

async function createReport(payload) {
    const res = await fetch(`${API_BASE}/api/reports`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(await readErrorMessage(res, 'Failed to create report'));
    return res.json();
}

async function updateReport(id, payload) {
    const res = await fetch(`${API_BASE}/api/reports/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(await readErrorMessage(res, 'Failed to update report'));
    return res.json();
}

async function deleteReportById(id) {
    const res = await fetch(`${API_BASE}/api/reports/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Failed to delete report');
}

async function generatePeriodicReport(type) {
    const res = await fetch(`${API_BASE}/api/reports/generate/${type}`, { method: 'POST' });
    if (!res.ok) throw new Error(await readErrorMessage(res, 'Failed to generate report'));
    return res.json();
}

function validateReportPayload(payload, options = {}) {
    const errors = [];
    if (!payload.title || !payload.title.trim()) errors.push('Report title is required.');
    if (!payload.reportType || !payload.reportType.trim()) errors.push('Report type is required.');
    if (options.requireDateRange) {
        if (!payload.startDate) errors.push('Start date is required.');
        if (!payload.endDate) errors.push('End date is required.');
    }
    if (options.requireSelection && (!payload.reportData || payload.reportData === '[]')) {
        errors.push('Select at least one record for this report.');
    }
    return errors;
}
