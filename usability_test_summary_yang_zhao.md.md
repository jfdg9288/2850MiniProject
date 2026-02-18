# Usability Test Summary — SCENARIO-BASED (NOT REAL USER DATA)

**Author:** Yang Zhao  
**Participants:** 3 (fictional placeholders: Alex Chen, Maya Patel, Daniel Kim)  
**Scope:** Homepage usability (Search + listing info + status + navigation)  
**Date:** 16/02/2026  

> **Important:** This summary is **scenario-based** and **NOT** derived from real user sessions.

## 1) Overview
- **What was evaluated:** Homepage wireframe/prototype focusing on search clarity, result readability, availability status visibility, and navigation discoverability.  
- **How many participants:** 3 placeholders used for scenario-based walkthrough.  
- **How long each session took:** ~10 minutes (estimated).

## 2) Top Issues (ranked)

### High severity
1. **Issue:** No-results feedback is unclear / page may appear blank  
   **Evidence:** (e.g., “It just looks blank.”) Users may interpret a blank state as a system failure.  
   **Recommendation:** Display a clear **“No results found”** message with next steps and a **Clear search** action.

2. **Issue:** Typo handling / recovery is weak (users assume it’s broken)  
   **Evidence:** (e.g., “Nothing’s coming up… maybe it’s broken?”) With minor typos, users may give up without guidance.  
   **Recommendation:** Add typo-tolerant matching (if feasible) or guidance text: “Check spelling / try fewer keywords / try author name”.

### Medium severity
3. **Issue:** Search submission and search scope are unclear  
   **Evidence:** Users may hesitate about Enter vs button and whether author search is supported.  
   **Recommendation:** Add a visible **Search** button and placeholder text: **“Search by title, author, ISBN…”** plus 1–2 example queries.

4. **Issue:** Availability status is easy to miss and “Avail” is unclear  
   **Evidence:** Users may miss status while scanning titles/authors; abbreviations reduce clarity.  
   **Recommendation:** Use **Available/Unavailable** and display as a prominent badge beside each result.

### Low severity
5. **Issue:** Right-side navigation does not clearly look interactive  
   **Evidence:** Users may not treat the right panel as navigation if it lacks button/link styling.  
   **Recommendation:** Style items as clickable navigation list items (hover/active states) with a heading such as **“My Account”**.

## 3) General Observations
- **What users looked at first:** Search area (top-left) and page title; some users scan the right panel first.  
- **Where users got stuck most:** Submitting searches, understanding “no results”, interpreting availability status, and recognising navigation items as clickable.  
- **Common expectations:** Clear search scope, explicit no-results state, visible availability per item, and obvious navigation controls.

## 4) Suggested Next Steps
- **UI changes to implement first:**  
  1) Add **No results** + **Empty input** feedback  
  2) Add **Search button** + placeholder (“Search by title, author, ISBN…”)  
  3) Make status a clear badge (“Available/Unavailable”) beside each result  
  4) Improve result layout for scanability (Title—Author; Year | ISBN | Status)  
  5) Restyle right navigation as clearly clickable items  

- **What to validate in a second round of testing:**  
  Run 3 short real sessions and validate: search clarity, no-results understanding, availability visibility, and navigation discoverability.
