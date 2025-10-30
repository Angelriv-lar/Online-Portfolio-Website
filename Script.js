/*
  WHAT DOES THIS FILE DO?
  - Opens/closes the hamburger (mobile) menu
  - Smoothly scrolls to sections for the arrow/buttons
  - Wires "Download CV" to open your PDF
  - Keeps the footer year up to date automatically
*/

/* ========= HAMBURGER MENU TOGGLE ========= */
/*
  We add/remove a CSS class named "open".
  CSS watches for that class and animates the menu and the three bars.
*/
function toggleMenu() {
  const menu = document.querySelector(".menu-links");
  const icon = document.querySelector(".hamburger-icon");
  const btn = document.getElementById("hamburger-button");

  // Toggle the "open" class on both the menu and the icon lines
  menu.classList.toggle("open");
  icon.classList.toggle("open");

  // Update the accessible state for screen readers
  const isOpen = icon.classList.contains("open");
  btn.setAttribute("aria-expanded", String(isOpen));
}

/* ========= STARTUP: HOOK UP BUTTONS & LINKS ========= */
document.addEventListener("DOMContentLoaded", () => {
  // 1) Hamburger button opens/closes the mobile menu
  const hamburgerBtn = document.getElementById("hamburger-button");
  if (hamburgerBtn) {
    hamburgerBtn.addEventListener("click", toggleMenu);
  }

  // 2) Clicking a mobile menu link should also close the menu (nicer UX)
  document.querySelectorAll("[data-close-menu]").forEach((link) => {
    link.addEventListener("click", () => {
      const menu = document.querySelector(".menu-links");
      const icon = document.querySelector(".hamburger-icon");
      const btn = document.getElementById("hamburger-button");
      menu.classList.remove("open");
      icon.classList.remove("open");
      btn.setAttribute("aria-expanded", "false");
    });
  });

  // 3) "Download CV" button opens your resume PDF in a new tab
  const cvBtn = document.querySelector("[data-download-cv]");
  if (cvBtn) {
    cvBtn.addEventListener("click", () => {
      // Replace with your real file if needed
      window.open("./assets/resume-example.pdf", "_blank", "noopener");
    });
  }

  // 4) "Contact Info" button jumps down to the contact section
  document.querySelectorAll("[data-jump-contact]").forEach((el) => {
    el.addEventListener("click", () => {
      document.getElementById("contact")?.scrollIntoView({ behavior: "smooth" });
    });
  });

  // 5) Arrow buttons (little chevrons) that jump to the next section
  document.querySelector("[data-jump-experience]")?.addEventListener("click", () => {
    document.getElementById("experience")?.scrollIntoView({ behavior: "smooth" });
  });

  document.querySelector("[data-jump-projects]")?.addEventListener("click", () => {
    document.getElementById("projects")?.scrollIntoView({ behavior: "smooth" });
  });

  // 6) Update the footer year automatically so you don't have to each January :)
  const yearEl = document.getElementById("year");
  if (yearEl) {
    yearEl.textContent = new Date().getFullYear();
  }
});


document.addEventListener('DOMContentLoaded', () => {
  const sections = Array.from(document.querySelectorAll('main > section'));
  const arrowBtn = document.querySelector('button.icon.arrow');
  if (arrowBtn) {
    arrowBtn.addEventListener('click', () => {
      const y = window.scrollY, buffer = 8;
      const next = sections.find(s => s.getBoundingClientRect().top + window.scrollY > y + buffer) || sections[0];
      next?.scrollIntoView({ behavior: 'smooth', block: 'start' });
    });
  }
});


/* For everything to fade/slide */
document.addEventListener('DOMContentLoaded', () => {
  // 1) auto-tag everything in <main> with .reveal
  document.querySelectorAll('body *').forEach(el => el.classList.add('reveal'));

  // 2) reveal when in view (and also on initial load for what's visible)
  const io = new IntersectionObserver((entries) => {
    entries.forEach(e => {
      if (e.isIntersecting) {
        e.target.classList.add('show');
        io.unobserve(e.target); // reveal once
      }
    });
  }, { threshold: 0.12 });

  document.querySelectorAll('.reveal').forEach(el => io.observe(el));
});

document.addEventListener('DOMContentLoaded', () => {
  const els = document.querySelectorAll('.reveal');
  const io = new IntersectionObserver((entries) => {
    entries.forEach(e => {
      if (e.isIntersecting) {
        e.target.classList.add('show');
        io.unobserve(e.target); // reveal once
      }
    });
  }, { threshold: 0.12 });

  els.forEach(el => io.observe(el));
});
