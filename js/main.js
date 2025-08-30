// Main JavaScript functionality for INNOSTORE

// Store for registered ideas (in a real app, this would be a database)
let registeredIdeas = JSON.parse(localStorage.getItem('registeredIdeas')) || [];

// DOM Elements
const ideaModal = document.getElementById('ideaModal');
const ideaForm = document.getElementById('ideaForm');

// Modal Functions
function openIdeaForm() {
    ideaModal.style.display = 'block';
    document.body.style.overflow = 'hidden'; // Prevent background scroll
}

function closeIdeaForm() {
    ideaModal.style.display = 'none';
    document.body.style.overflow = 'auto'; // Restore scroll
    clearForm();
}

// Close modal when clicking outside
window.addEventListener('click', function(event) {
    if (event.target === ideaModal) {
        closeIdeaForm();
    }
});

// Close modal with Escape key
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape' && ideaModal.style.display === 'block') {
        closeIdeaForm();
    }
});

// Form Functions
function clearForm() {
    ideaForm.reset();
    hideSuccessMessage();
}

function submitIdea(event) {
    event.preventDefault();
    
    // Get form data
    const formData = new FormData(ideaForm);
    const ideaData = {
        id: Date.now(), // Simple ID generation
        title: formData.get('ideaTitle'),
        category: formData.get('category'),
        description: formData.get('description'),
        targetAudience: formData.get('targetAudience'),
        stage: formData.get('stage'),
        investment: formData.get('investment'),
        timeline: formData.get('timeline'),
        contactInfo: formData.get('contactInfo'),
        submittedAt: new Date().toISOString()
    };
    
    // Validate required fields
    if (!ideaData.title || !ideaData.category || !ideaData.description || !ideaData.stage || !ideaData.contactInfo) {
        alert('Please fill in all required fields.');
        return;
    }
    
    // Validate email format
    if (!isValidEmail(ideaData.contactInfo)) {
        alert('Please enter a valid email address.');
        return;
    }
    
    // Save idea
    registeredIdeas.push(ideaData);
    localStorage.setItem('registeredIdeas', JSON.stringify(registeredIdeas));
    
    // Show success message
    showSuccessMessage();
    
    // Clear form after a delay
    setTimeout(() => {
        closeIdeaForm();
    }, 2000);
    
    console.log('Idea registered:', ideaData);
}

function showSuccessMessage() {
    // Create success message if it doesn't exist
    let successMessage = document.querySelector('.success-message');
    if (!successMessage) {
        successMessage = document.createElement('div');
        successMessage.className = 'success-message';
        successMessage.innerHTML = '✓ Your idea has been registered successfully! We will contact you soon.';
        ideaForm.insertBefore(successMessage, ideaForm.firstChild);
    }
    successMessage.style.display = 'block';
}

function hideSuccessMessage() {
    const successMessage = document.querySelector('.success-message');
    if (successMessage) {
        successMessage.style.display = 'none';
    }
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Navigation Functions
function initNavigation() {
    // Smooth scrolling for navigation links
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href').substring(1);
            const targetElement = document.getElementById(targetId);
            
            if (targetElement) {
                targetElement.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
    
    // Login button functionality
    const loginBtn = document.querySelector('.btn-login');
    loginBtn.addEventListener('click', function() {
        alert('Login functionality would be implemented here.');
    });
    
    // Register button functionality
    const registerBtn = document.querySelector('.btn-register');
    registerBtn.addEventListener('click', function() {
        alert('User registration functionality would be implemented here.');
    });
    
    // Innovators button functionality
    const innovatorsBtn = document.querySelector('.btn-innovators');
    innovatorsBtn.addEventListener('click', function() {
        showRegisteredIdeas();
    });
}

// Show registered ideas (for innovators)
function showRegisteredIdeas() {
    if (registeredIdeas.length === 0) {
        alert('No ideas have been registered yet. Be the first to submit an idea!');
        return;
    }
    
    // Create a simple display of ideas (in a real app, this would be a proper page)
    let ideasDisplay = 'Registered Ideas:\n\n';
    registeredIdeas.forEach((idea, index) => {
        ideasDisplay += `${index + 1}. ${idea.title}\n`;
        ideasDisplay += `   Category: ${idea.category}\n`;
        ideasDisplay += `   Stage: ${idea.stage}\n`;
        ideasDisplay += `   Description: ${idea.description.substring(0, 100)}...\n\n`;
    });
    
    alert(ideasDisplay);
}

// Utility Functions
function formatDate(dateString) {
    const options = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    };
    return new Date(dateString).toLocaleDateString('en-US', options);
}

// Admin Functions (for demonstration)
function clearAllIdeas() {
    if (confirm('Are you sure you want to clear all registered ideas? This cannot be undone.')) {
        registeredIdeas = [];
        localStorage.removeItem('registeredIdeas');
        alert('All ideas have been cleared.');
    }
}

function exportIdeas() {
    if (registeredIdeas.length === 0) {
        alert('No ideas to export.');
        return;
    }
    
    const dataStr = JSON.stringify(registeredIdeas, null, 2);
    const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr);
    
    const exportFileDefaultName = `ideas-export-${new Date().toISOString().split('T')[0]}.json`;
    
    const linkElement = document.createElement('a');
    linkElement.setAttribute('href', dataUri);
    linkElement.setAttribute('download', exportFileDefaultName);
    linkElement.click();
}

// Initialize everything when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initNavigation();
    
    // Add keyboard shortcuts for admin functions (Ctrl+Shift+C to clear, Ctrl+Shift+E to export)
    document.addEventListener('keydown', function(e) {
        if (e.ctrlKey && e.shiftKey) {
            if (e.key === 'C') {
                clearAllIdeas();
            } else if (e.key === 'E') {
                exportIdeas();
            }
        }
    });
    
    console.log('INNOSTORE application initialized successfully!');
    console.log(`Currently ${registeredIdeas.length} ideas registered.`);
});

// Make functions available globally
window.openIdeaForm = openIdeaForm;
window.closeIdeaForm = closeIdeaForm;
window.submitIdea = submitIdea;