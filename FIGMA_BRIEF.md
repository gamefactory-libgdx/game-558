# Figma AI Design Brief: Bounce Up

---

## 1. Art Style & Color Palette

**Art Style:** Bright, playful cartoon with rounded forms and soft edges. Character designs are chunky and expressive; platforms are geometric with subtle 3D perspective via gradient shading. Environment assets (clouds, rocks, stars) use solid fills with black outlines (2–3px stroke) for clarity on mobile screens. This is a cheerful, accessible style targeting ages 8+ without being childish.

**Primary Color Palette:**
- Sky World: `#87CEEB` (sky blue), `#FFD700` (golden yellow), `#FFFFFF` (white)
- Cave World: `#4A4A4A` (dark gray), `#8B4513` (saddle brown), `#FF6347` (tomato red for lava)
- Space World: `#0B1929` (deep navy), `#9D4EDD` (neon purple), `#00D9FF` (cyan)

**Accent Colors:** `#FFD700` (gold for stars/collectibles), `#FF1493` (hot pink for danger/hazards)

**Typography Mood & Weight:** Sans-serif, bold (700 weight) for headers and scores (Arial Black or equivalent), medium (500 weight) for body text and button labels. All text uses white (`#FFFFFF`) or black (`#1A1A1A`) depending on background luminance, with a subtle black text shadow (2px offset, 40% opacity) for readability over complex backgrounds.

---

## 2. App Icon — icon_512.png (512×512px)

**Background:** Linear gradient from `#FFD700` (top-left) to `#FF6B9D` (bottom-right), creating a vibrant sunset energy that represents all three worlds. Subtle radial glow at center (`#FFFFFF` at 20% opacity) adds depth.

**Central Symbol:** A stylized bouncing character (simple rounded rectangle head with dot eyes and smiling mouth) captured mid-bounce, with an upward-pointing arrow trail behind it in bright `#00D9FF` (cyan). The character is `#FF1493` (hot pink) with a gradient from lighter pink at top to darker pink at bottom. The arrow trail curves gently left and right, suggesting the tilt mechanic.

**Glow/Shadow Effects:** Outer glow around the character (4px, `#FF1493` at 30% opacity) emphasizes the action. Inner shadow on the gradient background (subtle, 2px inset, `#000000` at 15% opacity) adds dimensionality. A thin white stroke (3px) outlines the character for mobile clarity.

**Overall Mood:** Energetic, optimistic, and instantly recognizable as an action/movement game. The character is centered within the safe 400×400px zone. No text, no letters.

---

## 3. Backgrounds (480×854 portrait)

**Identified Worlds/Themes:**
1. Main Menu (neutral)
2. Sky World
3. Cave World
4. Space World

### Background Files List:
- backgrounds/bg_main.png
- backgrounds/bg_sky.png
- backgrounds/bg_cave.png
- backgrounds/bg_space.png

---

### backgrounds/bg_main.png (480×854)
A sweeping gradient from `#87CEEB` (sky blue, top) to `#E0FFFF` (light cyan, bottom) with a layered parallax effect. Foreground shows 3–4 fluffy white clouds (`#FFFFFF` with subtle `#F0F0F0` shadows) positioned at varying depths. Mid-ground includes a distant mountain silhouette in `#B0C4DE` (light slate blue). A warm sun (`#FFD700`, 120px diameter) sits in the upper-right corner with a soft glow halo. Faint stars (`#FFFF99`, 2–4px dots) peek through the sky. The overall mood is welcoming and adventurous, setting up the three-world journey.

---

### backgrounds/bg_sky.png (480×854)
Bright, open sky dominating 70% of the canvas using a vertical gradient from `#87CEEB` (top) to `#E0FFFF` (bottom). Scattered puffy clouds (`#FFFFFF` with black outline, 2px) at varied x-positions and y-depths create parallax visual interest. A few birds (simple V-shaped silhouettes in `#696969` dark gray) fly across the mid-section. Rolling green hills (`#90EE90` light green) anchor the bottom 20% with a subtle wave pattern. Sunlight rays (thin lines in `#FFD700` at 20% opacity, radiating from top-right) add atmospheric warmth. No hazards visible—this is the calm, inviting first world.

---

### backgrounds/bg_cave.png (480×854)
Dark, cavernous environment with a gradient from `#2F4F4F` (dark slate gray, top) to `#1C1C1C` (near black, bottom). Craggy rock formations (`#4A4A4A` with black outline, 3px) jut from top and sides, creating a claustrophobic feel. Glowing lava pools (`#FF6347` tomato red with `#FFD700` highlights, 8–12px glows) appear at scattered depths, suggesting danger and heat. Stalactites hang from the ceiling (dark brown `#8B4513` with sharp points). A few bones or broken platforms (`#D3D3D3` light gray) hint at past failures. Overall mood: ominous, challenging, and heating up.

---

### backgrounds/bg_space.png (480×854)
Deep outer space with a dark gradient from `#0B1929` (top) to `#0A0E27` (bottom). Scattered stars (`#FFFFFF` and `#FFD700`, 1–3px dots) distributed naturally across the canvas, creating constellations. Distant nebula clouds in soft `#9D4EDD` (neon purple) and `#00D9FF` (cyan) swirl in the mid-ground at low opacity (15–20%), suggesting cosmic depth. A few asteroids (irregular gray circles in `#696969` with black shadow, 40–80px diameter) drift across the scene at various depths. A faint ringed planet or black hole silhouette (`#4A4A4A` with cyan ring) floats in the lower-right. Overall mood: vast, mysterious, alien, and electrifying.

---

## 4. UI Screens (480×854 portrait)

---

### main_menu.png (480×854)
Uses **bg_main.png** as the backdrop. Centered at top (y: 80px) is the game title "BOUNCE UP" in bold white (`#FFFFFF`) with a soft black shadow (3px offset). Below the title at y: 140px are four large button zones arranged vertically, each 80px tall with rounded corners and shadow depth: **PLAY** (gradient `#FFD700` to `#FFA500`), **SHOP** (gradient `#FF1493` to `#C71585`), **LEADERBOARD** (gradient `#00D9FF` to `#0099CC`), and **SETTINGS** (gradient `#9D4EDD` to `#6A0572`). At the bottom (y: 750px) is a smaller **CREDITS** button in gray. Button text is white, bold, centered. A subtle rotating star icon (cosmetic, not clickable) spins above the PLAY button to draw focus.

---

### world_select.png (480×854)
Uses **bg_main.png** as the backdrop. Header "SELECT WORLD" appears at top in white bold text (y: 50px). Three world cards are arranged vertically, each 120px tall, with rounded corners and depth shadow. **Card 1 (Sky):** bg_sky.png thumbnail on left, "SKY WORLD" label in white, "Unlock: Height 100m" in small gray text, **PLAY** button in golden yellow on right. **Card 2 (Cave):** bg_cave.png thumbnail, "CAVE WORLD" label in white, "Unlock: Height 300m" in small gray text, **PLAY** button (or **LOCKED** overlay in red if not yet unlocked). **Card 3 (Space):** bg_space.png thumbnail, "SPACE WORLD" label in white, "Unlock: Height 500m" in small gray text, **PLAY** button (or **LOCKED** overlay). A **BACK** button appears at bottom in gray.

---

### game_sky.png (480×854)
Uses **bg_sky.png** as the backdrop. The gameplay viewport occupies 90% of the screen. At the **top-left** (y: 10px, x: 10px) is a height counter: "HEIGHT: 250m" in white bold text with a black shadow. **Top-right** shows the star counter: "⭐ 12" in white text with the star icon in `#FFD700`. **Center-screen** during play shows the bouncing character (auto-drawn by game engine) and platforms (auto-drawn). A subtle **tilt indicator** (two vertical bars on left and right edges, very faint cyan `#00D9FF` at 10% opacity) shows controller responsiveness. At the **bottom-center**, a small **PAUSE** icon (cosmetic, clickable) appears in white. No buttons obscure the playfield; all UI is translucent or edge-positioned.

---

### game_cave.png (480×854)
Uses **bg_cave.png** as the backdrop. Identical HUD layout to game_sky.png: height counter top-left in white, star counter top-right in white with gold star. The playfield is darker, with red hazard indicators (lava, spikes) more prominent. **Bottom-center** shows the same **PAUSE** icon. The tilt indicator bars are slightly more visible (cyan at 15% opacity) to aid navigation in the cramped cave environment. Character sprite may appear darker or with a glow effect to stand out against the dark background.

---

### game_space.png (480×854)
Uses **bg_space.png** as the backdrop. Height counter and star counter positioned identically: top-left and top-right in white. The **tilt indicator bars** (left and right edges) are more prominent in cyan (`#00D9FF`) at 20% opacity, given the open space environment. A **score multiplier indicator** (e.g., "2x COMBO") may appear center-top in neon purple (`#9D4EDD`) when the player collects consecutive stars. The **PAUSE** icon remains bottom-center in white. Overall, the HUD is minimal but slightly more vibrant due to the cosmic color palette.

---

### game_over.png (480×854)
Uses **bg_main.png** as the backdrop with a semi-transparent dark overlay (`#000000` at 70% opacity) covering the entire screen to focus attention. Centered on screen: **"GAME OVER"** in large white bold text (y: 100px). Below it (y: 180px): **"FINAL HEIGHT: 487m"** in white medium text, followed by **"STARS EARNED: 23"** in gold text (y: 220px), and **"BEST HEIGHT: 652m"** in small gray text (y: 260px). Two large buttons at bottom (y: 650px): **PLAY AGAIN** (golden gradient, left-aligned) and **MAIN MENU** (purple gradient, right-aligned), each 180px wide. A small star animation loops around the STARS EARNED line to reinforce reward feedback.

---

### leaderboard.png (480×854)
Uses **bg_main.png** as the backdrop. Header **"TOP 10 SCORES"** in white bold text at top (y: 50px). Below is a vertical list occupying y: 120px to y: 750px, with 10 ranked rows. Each row: rank number (`#FFD700`) on left, player name in white (30px tall, single-line), and height in gray text on right. Rows alternate subtle background tint (`#FFFFFF` at 5% opacity) for readability. The current player's score (if in top 10) is highlighted with a light gold background tint. At bottom (y: 800px) is a **BACK** button in gray.

---

### shop.png (480×854)
Uses **bg_main.png** as the backdrop. Header **"SHOP"** in white bold text at top (y: 50px). Subtitle **"CHARACTER SKINS"** in small gray text (y: 90px). A 2×3 grid of character skin cards occupies the center (y: 120px to y: 750px), each card 200×180px with rounded corners and shadow. **Per card:** Skin preview artwork (e.g., colored character variant), skin name below in white text, price or "UNLOCKED" status in gold or gray text, and a button zone (either **EQUIP** if owned, or **PURCHASE** with a star cost, or **LOCKED** if unavailable). Card backgrounds are semi-transparent dark (`#000000` at 40% opacity). At bottom (y: 800px) is a **BACK** button in gray, and at bottom-left is a small info text: **"Currency: ⭐ 156"** in white.

---

### settings.png (480×854)
Uses **bg_main.png** as the backdrop. Header **"SETTINGS"** in white bold text at top (y: 50px). A vertical list of toggle options occupies y: 120px to y: 600px, each 60px tall. Three toggles: **Sound: ON** (toggle switch on right, green when on), **Music: ON** (toggle switch, green when on), **Vibration: ON** (toggle switch, green when on). Below at y: 650px: small text **"Game Version: 1.0.0"** in gray. At y: 700px: a **RESET DATA** button in red (smaller, warning-styled). At bottom (y: 800px) is a **BACK** button in gray. All text is white unless noted.

---

### credits.png (480×854)
Uses **bg_main.png** as the backdrop. Header **"CREDITS"** in white bold text at top (y: 50px). A scrollable list (y: 120px to y: 750px, single-column layout) in white and gray text: **Design:** [names], **Programming:** [names], **Art:** [names], **Audio:** [names], **Libraries:** libGDX, and any third-party assets. Text is left-aligned, small (12–14px), with subtle section headers in gold (`#FFD700`). At bottom (y: 800px) is a **BACK** button in gray.

---

## 5. Export Checklist

- icon_512.png (512×512)
- backgrounds/bg_main.png (480×854)
- backgrounds/bg_sky.png (480×854)
- backgrounds/bg_cave.png (480×854)
- backgrounds/bg_space.png (480×854)
- main_menu.png (480×854)
- world_select.png (480×854)
- game_sky.png (480×854)
- game_cave.png (480×854)
- game_space.png (480×854)
- game_over.png (480×854)
- leaderboard.png (480×854)
- shop.png (480×854)
- settings.png (480×854)
- credits.png (480×854)

**Total: 15 files**

---

**End of Design Brief**
