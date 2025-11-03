<template>
  <router-view />
</template>

<script setup>
// Root component renders the current route
</script>

<style>
:root {
  --bg: #0f172a;
  --panel: #111827;
  --text: #e5e7eb;
  --accent: #22c55e;
}
html, body, #app {
  height: 100%;
}
body {
  margin: 0;
  background: var(--bg);
  color: var(--text);
  font-family: ui-sans-serif, system-ui, -apple-system, Segoe UI, Roboto,
    Helvetica, Arial, "Apple Color Emoji", "Segoe UI Emoji";
}
</style>