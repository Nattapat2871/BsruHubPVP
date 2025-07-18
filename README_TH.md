# BsruHubPVP


<div align="center">

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![GitHub Repo stars](https://img.shields.io/github/stars/Nattapat2871/BsruHubPVP?style=flat-square)](https://github.com/Nattapat2871/BsruHubPVP/stargazers)
![Visitor Badge](https://api.visitorbadge.io/api/VisitorHit?user=Nattapat2871&repo=BsruHubPVP&countColor=%237B1E7A&style=flat-square)

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/Nattapat2871)

</div>

<p align= "center">
        <a href="/README.md">English</a>   <b>ภาษาไทย</b>　



BsruHubPVP คือปลั๊กอินสำหรับจัดการระบบ PVP ขั้นสูงที่ออกแบบมาสำหรับเซิร์ฟเวอร์ Hub หรือเซิร์ฟเวอร์จัดอีเวนต์โดยเฉพาะ โดยจะสร้างโซน PVP ที่ผู้เล่นต้องแสดงเจตจำนงเพื่อเข้าร่วมการต่อสู้ด้วยตัวเอง ผ่านการถือไอเทมที่กำหนดเพื่อรับชุดเกราะพิเศษสำหรับต่อสู้ ทำให้การต่อสู้มีความยุติธรรมและสนุกสนานยิ่งขึ้น ระบบทั้งหมดสามารถปรับแต่งได้อย่างเต็มที่ ตั้งแต่ไอเทม, ข้อความ, เสียง, ไปจนถึงบอร์ดจัดอันดับ

---

## ✨ ความสามารถหลัก (Features)

- **PVP แบบสมัครใจ:** ผู้เล่นจะเข้าสู่โหมด PVP ด้วยการถือดาบพิเศษค้างไว้ตามเวลาที่กำหนด
- **ชุดเกราะอัตโนมัติ:** สวมชุดเกราะเพชรลงอาคมให้ผู้เล่นโดยอัตโนมัติเมื่อเข้าสู่โหมด PVP และถอดออกเมื่อออกจากโหมด
- **ควบคุมสภาพแวดล้อม:** การต่อสู้จะสร้างความเสียหายได้ก็ต่อเมื่อผู้เล่นทั้งสองฝ่ายอยู่ในโหมด PVP (สวมเกราะพิเศษ) เท่านั้น
- **ปรับแต่งได้เต็มรูปแบบ:** เกือบทุกอย่างของปลั๊กอินสามารถปรับแต่งได้ผ่านไฟล์ `config.yml` ไม่ว่าจะเป็นข้อความ, เสียง, ชื่อไอเทม, คำอธิบาย, และ Enchantment
- **ไอเทมพิเศษ:** ดาบ PVP จะไม่สามารถพังได้ และชุดเกราะพิเศษจะไม่สามารถถอดได้ด้วยตนเองระหว่างการต่อสู้
- **ระบบการตาย:** เมื่อตาย ของในตัวจะไม่ตก ยกเว้นดาบ PVP และจะไม่มีข้อความการตายแสดงในแชทเพื่อความสะอาดตา
- **แสดงพลังชีวิตสด:** ผู้เล่นที่กำลังต่อสู้จะเห็นหลอดเลือดของคู่ต่อสู้บน Action Bar
- **บอร์ดจัดอันดับ Kills:** ติดตามสถิติการฆ่าและมี Placeholder สำหรับแสดงผลผ่าน PlaceholderAPI
- **คำสั่งสำหรับแอดมิน:** มีคำสั่งสำหรับรับไอเทมและรีโหลดปลั๊กอิน พร้อมระบบช่วยพิมพ์ (Tab Completion)

---

## 📋 สิ่งที่ต้องมี (Requirements)

- **ตัวรันเซิร์ฟเวอร์:** PaperMC 1.21+ (แนะนำ)
- **Java:** เวอร์ชั่น 21+
- **ปลั๊กอินที่ต้องใช้ร่วมกัน:**
    - [**PlaceholderAPI**](https://www.spigotmc.org/resources/placeholderapi.6245/): จำเป็นต้องมีเพื่อให้ Placeholder แสดงผลได้

---

## 🚀 การติดตั้ง

1.  ดาวน์โหลดไฟล์ `BsruHubPVP.jar` จากหน้า [releases](https://github.com/nattapat2871/BsruHubPVP/releases)
2.  ติดตั้งปลั๊กอิน **PlaceholderAPI** ลงบนเซิร์ฟเวอร์ของคุณ
3.  นำไฟล์ `BsruHubPVP.jar` ไปไว้ในโฟลเดอร์ `/plugins` ของเซิร์ฟเวอร์
4.  เปิดหรือรีสตาร์ทเซิร์ฟเวอร์เพื่อให้ปลั๊กอินสร้างไฟล์ตั้งค่าเริ่มต้น
5.  เข้าไปแก้ไขไฟล์ `config.yml` และ `kills.yml` ได้ตามต้องการ
6.  ใช้คำสั่ง `/bsruhubpvp reload` เพื่อให้การตั้งค่าใหม่มีผลทันทีโดยไม่ต้องรีสตาร์ท

---

## ⚙️ คำสั่งและสิทธิ์ (Commands & Permissions)

| คำสั่ง                     | สิทธิ์ (Permission)            | คำอธิบาย                                        |
| --------------------------- | ----------------------------- | ------------------------------------------------ |
| `/bsruhubpvp`               | `bsruhubpvp.command.use`      | แสดงข้อมูลเกี่ยวกับปลั๊กอิน (ใช้ได้ทุกคน)       |
| `/bsruhubpvp getsword`      | `bsruhubpvp.command.getsword` | รับดาบ PVP พิเศษ (สำหรับ OP)                 |
| `/bsruhubpvp reload`        | `bsruhubpvp.command.reload`   | รีโหลดไฟล์ `config.yml` ของปลั๊กอิน (สำหรับ OP) |

---

## 📊 เพลสโฮลเดอร์ (Placeholders)

คุณต้องติดตั้ง PlaceholderAPI ก่อนจึงจะสามารถใช้ตัวแปรเหล่านี้ได้

-   `%bsruhubpvp_topkill_1%` - แสดงผู้เล่นที่มี Kills สูงสุดเป็นอันดับ 1
-   `%bsruhubpvp_topkill_2%` - แสดงผู้เล่นที่มี Kills สูงสุดเป็นอันดับ 2
-   `%bsruhubpvp_topkill_N%` - แสดงผู้เล่นอันดับที่ N ในบอร์ดจัดอันดับ

คุณสามารถเปลี่ยนรูปแบบการแสดงผลได้ใน `config.yml`

---

## 📝 การตั้งค่า (`config.yml`)

ด้านล่างนี้คือไฟล์ตั้งค่าเริ่มต้นพร้อมคำอธิบายโดยละเอียดสำหรับแต่ละหัวข้อ

```yaml
# ------------------------------------
# การตั้งค่าสำหรับปลั๊กอิน BsruHubPVP
# ------------------------------------
# ตัวแปรที่ใช้ในข้อความได้: %player%, %time%, %kills%
# ข้อความทั้งหมดรองรับโค้ดสีด้วยเครื่องหมาย &
# รายชื่อเสียงทั้งหมด: [https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html)

# การตั้งค่าทั่วไป
settings:
  # เวลานับถอยหลัง (หน่วยเป็นวินาที)
  countdown-seconds: 5

# การตั้งค่าไอเทม
items:
  sword:
    # ชื่อที่แสดงของดาบ PVP
    name: "&c&lดาบ PVP"
    # คำอธิบาย (Lore) ของดาบ
    lore:
      - "&7ดาบสำหรับผู้ที่พร้อมจะต่อสู้"
    # Enchantment ของดาบ
    enchantments:
      sharpness-level: 5
  armor:
    # ชื่อที่แสดงของชุดเกราะพิเศษ (ทุกชิ้น)
    name: "&a&lเกราะศักดิ์สิทธิ์"
    # คำอธิบาย (Lore) ของชุดเกราะ
    lore:
      - "&7เกราะที่ถูกตีขึ้นเพื่อแชมป์เปี้ยน"
      - "&7แห่งการประลอง"
    # Enchantment ของชุดเกราะ
    enchantments:
      protection-level: 2
      unbreaking-level: 10

# ข้อความทั้งหมดที่จะแสดงให้ผู้เล่นเห็น
messages:
  # คำนำหน้าข้อความในแชททั้งหมดจากปลั๊กอินนี้
  prefix: "&8[&bBSRU&fPVP&8] &r"
  # ข้อความเมื่อไม่มีสิทธิ์ใช้คำสั่ง
  no-permission: "%prefix%&cคุณไม่มีสิทธิ์ใช้คำสั่งนี้"
  # ข้อความเมื่อใช้คำสั่ง /getsword สำเร็จ
  get-sword-success: "%prefix%&aคุณได้รับ &eดาบ PVP&a!"
  # ข้อความเมื่อใช้คำสั่ง /reload สำเร็จ
  reload-success: "%prefix%&aรีโหลดการตั้งค่าเรียบร้อยแล้ว"
  
  # ข้อความนับถอยหลัง (แสดงบน Action Bar)
  countdown:
    equip: "&aกำลังสวมเกราะในอีก &e%time% &aวินาที..."
    equip-cancel: "&cยกเลิกการสวมเกราะ"
    equip-success: "&bคุณได้เข้าสู่โหมด PVP แล้ว!"
    unequip: "&eกำลังถอดเกราะในอีก &e%time% &eวินาที..."
    unequip-cancel: "&aยกเลิกการถอดเกราะ"
    unequip-success: "&7คุณได้ออกจากโหมด PVP"
    
  # ข้อความเกี่ยวกับการต่อสู้
  pvp:
    cant-remove-armor: "&cไม่สามารถถอดเกราะนี้ได้ขณะอยู่ในโหมด PVP"
    attacker-not-ready: "&cคุณต้องอยู่ในโหมด PVP ก่อนถึงจะสู้ได้"
    victim-not-ready: "&eผู้เล่น %player% ยังไม่พร้อมต่อสู้"
    # รูปแบบการแสดงพลังชีวิต (%.1f คือค่าพลังชีวิต เช่น 18.5)
    health-display: "&c❤ %.1f &8| &e%player%"

# รูปแบบข้อความสำหรับ PlaceholderAPI
placeholders:
  # รูปแบบของ Placeholder จัดอันดับ Kills
  top-kill-format: "%player% &7- &e%kills% Kills"
  # ข้อความที่จะแสดงเมื่ออันดับที่ขอนั้นยังไม่มีข้อมูล
  top-kill-not-available: "ไม่มีข้อมูล"

# เสียงประกอบทั้งหมดของปลั๊กอิน
sounds:
  countdown-tick: "UI_BUTTON_CLICK"
  equip-success: "ENTITY_PLAYER_LEVELUP"
  unequip-success: "ITEM_ARMOR_EQUIP_GENERIC"

```

---

## 🧑‍💻 ผู้พัฒนา

สร้างสรรค์โดย **Nattapat2871**.
- **GitHub:** [https://github.com/nattapat2871/BsruHubPVP](https://github.com/nattapat2871/BsruHubPVP)