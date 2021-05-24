# EconRepair
EconRepair is a plugin for charging fine-tuned repair costs based on the amount of damage an item has with high configurability.

Requires [Vault](https://www.spigotmc.org/resources/vault.34315/) and a compatible economy plugin.

**Features:**

* Repair the item in hand or all items in inventory
* Get a quote on how much repairs will cost before committing
* Configurable low durability warning
* Set cost per durability point for repairs
* Set cost multiplier for enchanted items
* Set whether or not enchant multiplier is compounded for each enchant on the item
* Set discounts for different permissions groups

**Commands:**

* **/econrepair (help | reload)** - Get help or reload config
* **/repair (all | quote)** - Repair item in hand, all items in inventory, or get a quote for how much repairs will cost

**Permissions:**

All perms default to OP-only

* **econrepair.repair** - Repair item in hand.
* **econrepair.repair.all** - Repair all items in inventory, includes econrepair.repair
* **econrepair.repair** - Get a repair quote, includes econrepair.repair
* **econrepair.reload** - Reload plugin configuration
