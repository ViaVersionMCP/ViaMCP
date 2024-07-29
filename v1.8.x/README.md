# ViaMCP
ViaVersion VersionSwitcher for Minecraft Coder Pack (MCP)
<!-- TOC -->
* [ViaMCP](#viamcp)
  * [Contact](#contact)
  * [Setup](#setup)
    * [Main-Class](#main-class)
    * [NetworkManager](#networkmanager)
  * [Version Control](#version-control)
    * [Version Slider](#version-slider)
  * [Clientside Fixes](#clientside-fixes)
    * [Attack Order Fixes](#attack-order-fixes)
    * [Block Sound Fixes](#block-sound-fixes)
    * [Transaction Fixes for 1.17+](#transaction-fixes-for-117)
  * [Exporting Without JAR Files](#exporting-without-jar-files)
<!-- TOC -->
## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/FlorianMichael/ViaMCP/issues).  
If you just want to talk or need help with ViaMCP feel free to join my
[Discord](https://discord.gg/BwWhCHUKDf).

# Updating notice for existing users (if you are new to ViaMCP, you can ignore this)
ViaVersion 4.10.0 did some changes to the ProtocolVersion API, you have to update your own code if you ever used the ViaLoadingBase class:
```java
// Old
ViaLoadingBase.getInstance().getTargetVersion().isOlderThan(ProtocolVersion.v1_8);
ViaLoadingBase.getInstance().getTargetVersion().isNewerThan(ProtocolVersion.v1_8);
ViaLoadingBase.getInstance().getTargetVersion().isNewerThanOrEqualTo(ProtocolVersion.v1_8);
ViaLoadingBase.getInstance().getTargetVersion().isOlderThanOrEqualTo(ProtocolVersion.v1_8);

ViaLoadingBase.getInstance().getTargetVersion().getIndex();

// New
ViaLoadingBase.getInstance().getTargetVersion().olderThan(ProtocolVersion.v1_8);
ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8);
ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_8);
ViaLoadingBase.getInstance().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8);

ViaLoadingBase.PROTOCOLS.indexOf(ViaLoadingBase.getInstance().getTargetVersion());
```
In addition to that, the *ComparableProtocolVersion* class has been removed and it's methods have been moved to the *ProtocolVersion* class.

## Setup
Firstly, you will need to add the listed libraries into your dependencies in IntelliJ or Eclipse

Dependencies (Included inside ``libraries`` folder)
```
downgraded-ViaVersion-[ver].jar > ViaVersion > https://github.com/ViaVersion/ViaVersion
downgraded-ViaBackwards-[ver].jar > ViaBackwards > https://github.com/ViaVersion/ViaBackwards
downgraded-ViaRewind-[ver].jar > ViaRewind > https://github.com/ViaVersion/ViaRewind
snakeyml-2.2.jar > SnakeYaml > https://bitbucket.org/snakeyaml/snakeyaml
```

Secondly, you need to add code that allows you to actually use ViaMCP (**Choose the version folder that corresponds with your client version**)

For other versions than 1.8.x and 1.12.2, you will need to modify the code to fit your client version. You can see namings for
other major versions [here](https://github.com/ViaVersion/ViaForge)

NOTE:
ViaVersion 5.0.0+ doesn't support Java 8 anymore, therefore when updating the libraries yourself, you need to download
the -Java8 jar files from the [ci server](https://ci.viaversion.com/) or generate them yourself using [this](https://github.com/ViaVersion/ViaForge/tree/legacy-1.8?tab=readme-ov-file#installation) tool.

### Main-Class
Add this to the main class of your client (aka injection function)

```java
try {
    ViaMCP.create();
    
    // In case you want a version slider like in the Minecraft options, you can use this code here, please choose one of those:
          
    ViaMCP.INSTANCE.initAsyncSlider(); // For top left aligned slider
    ViaMCP.INSTANCE.initAsyncSlider(x, y, width (min. 110), height (recommended 20)); // For custom position and size slider
} catch (Exception e) {
    e.printStackTrace();
}
```

### NetworkManager
You will need to modify 2 methods inside NetworkManager.java

**1. Hook ViaVersion into the Netty Pipeline**

Find the method, that is ``func_181124_a``, ``createNetworkManagerAndConnect`` or contains ``(Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)lazyloadbase.getValue())``

Find the vanilla network pipeline call:
```java
// 1.8.x client
p_initChannel_1_.pipeline().addLast((String)"timeout", (ChannelHandler)(new ReadTimeoutHandler(30))).addLast((String)"splitter", (ChannelHandler)(new MessageDeserializer2())).addLast((String)"decoder", (ChannelHandler)(new MessageDeserializer(EnumPacketDirection.CLIENTBOUND))).addLast((String)"prepender", (ChannelHandler)(new MessageSerializer2())).addLast((String)"encoder", (ChannelHandler)(new MessageSerializer(EnumPacketDirection.SERVERBOUND))).addLast((String)"packet_handler", (ChannelHandler)networkmanager);

// 1.12.x client
p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new NettyVarint21FrameDecoder()).addLast("decoder", new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", new NettyVarint21FrameEncoder()).addLast("encoder", new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", networkmanager);
```

After the vanilla network pipeline call, add the ViaMCP protocol pipeline hook:
```java
if (p_initChannel_1_ instanceof SocketChannel && ViaLoadingBase.getInstance().getTargetVersion().getVersion() != ViaMCP.NATIVE_VERSION) {
    final UserConnection user = new UserConnectionImpl(p_initChannel_1_, true);
    new ProtocolPipelineImpl(user);
    
    p_initChannel_1_.pipeline().addLast(new MCPVLBPipeline(user));
}
```

Your code should look like this afterwards (1.8.x for example), the vanilla network pipeline call should not be commented out and the ViaMCP protocol pipeline hook should be after the vanilla network pipeline call:
```java
p_initChannel_1_.pipeline().addLast((String)"timeout", (ChannelHandler)(new ReadTimeoutHandler(30))).addLast((String)"splitter", (ChannelHandler)(new MessageDeserializer2())).addLast((String)"decoder", (ChannelHandler)(new MessageDeserializer(EnumPacketDirection.CLIENTBOUND))).addLast((String)"prepender", (ChannelHandler)(new MessageSerializer2())).addLast((String)"encoder", (ChannelHandler)(new MessageSerializer(EnumPacketDirection.SERVERBOUND))).addLast((String)"packet_handler", (ChannelHandler)networkmanager);

if (p_initChannel_1_ instanceof SocketChannel && ViaLoadingBase.getInstance().getTargetVersion().getVersion() != ViaMCP.NATIVE_VERSION) {
    final UserConnection user = new UserConnectionImpl(p_initChannel_1_, true);
    new ProtocolPipelineImpl(user);
    
    p_initChannel_1_.pipeline().addLast(new MCPVLBPipeline(user));
}
```

### If you want to send custom packets, you have to store the UserConnection instance in a variable for later, it's important that this variable is NOT STATIC since it's also used for pinging servers!

**2. Fix the compression in the NetworkManager#setCompressionTreshold function**

Simply call the following code at the end of the method in Minecraft:
```java
this.channel.pipeline().fireUserEventTriggered(new CompressionReorderEvent());
```

## Version Control
You will need to add a button to access the protocol switcher (or alternatively use the version slider under this section) <br>
In ``addSingleplayerMultiplayerButtons()`` function add (if in GuiMainMenu):
```java
this.buttonList.add(new GuiButton(69, 5, 5, 90, 20, "Version"));
```
In ``actionPerformed()`` function add:
```java
if (button.id == 69)
{
    this.mc.displayGuiScreen(new GuiProtocolSelector(this));
}
```
### Version Slider
You can also use a version slider to control ViaMCP versions
```java
this.buttonList.add(ViaMCP.INSTANCE.getAsyncVersionSlider());
```

## Clientside Fixes
### Attack Order Fixes
**Class: Minecraft.java** <br>
**Function: clickMouse()** <br>

**1.8.x** <br>
Replace ``this.thePlayer.swingItem();`` on the 1st line in the if-clause with:
```java
AttackOrder.sendConditionalSwing(this.objectMouseOver);
```
Replace ``this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);`` in the switch in case ``ENTITY`` with:
```java
AttackOrder.sendFixedAttack(this.thePlayer, this.objectMouseOver.entityHit);
```

**1.12.2** <br>
Replace ``this.player.swingArm(EnumHand.MAIN_HAND);`` at the last line in the else if-clause with:
```java
AttackOrder.sendConditionalSwing(this.objectMouseOver, EnumHand.MAIN_HAND);
```
Replace ``this.playerController.attackEntity(this.player, this.objectMouseOver.entityHit);`` in the switch in case ``ENTITY`` with:
```java
AttackOrder.sendFixedAttack(this.thePlayer, this.objectMouseOver.entityHit, EnumHand.MAIN_HAND);
```

### Block Sound Fixes
**Block Placement**

Replace all code in ``onItemUse`` function in the ``ItemBlock`` class with:
```java
return FixedSoundEngine.onItemUse(this, stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
```

**Block Breaking**

Replace all code in ``destroyBlock`` function in the ``World`` class with:
```java
return FixedSoundEngine.destroyBlock(this, pos, dropBlock);
```

### Transaction Fixes for 1.17+
Call the ``fixTransactions();`` in the ``ViaMCP`` class file so ViaVersion doesn't remap anything in transaction packets.

After that, you need to do some changes in the Game code:

**Class: S32PacketConfirmTransaction.java** <br>
**Function: readPacketData()** <br>

Replace the code with this method:
```java
public void readPacketData(PacketBuffer buf) throws IOException {
    if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17)) {
        this.windowId = buf.readInt();
    } else {
        this.windowId = buf.readUnsignedByte();
        this.actionNumber = buf.readShort();
        this.accepted = buf.readBoolean();
    }
}
```

**Class: C0FPacketConfirmTransaction.java** <br>
**Function: writePacketData()** <br>

Replace the code with this method:
```java
public void writePacketData(PacketBuffer buf) throws IOException {
    if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17)) {
        buf.writeInt(this.windowId);
    } else {
        buf.writeByte(this.windowId);
        buf.writeShort(this.uid);
        buf.writeByte(this.accepted ? 1 : 0);
    }
}
```

Note: this code can be different depending on your mappings and game version, you just need to make sure
it only reads the window id and doesn't read the rest of the packet because we previously removed the 
ViaVersion handlers which would have handled the rest of the packet.

**Class: NetHandlerPlayClient.java** <br>
**Function: handleConfirmTransaction()** <br>
 
Add this code after the checkThreadAndEnqueue function call:
```java
if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17)) {
    this.addToSendQueue(new C0FPacketConfirmTransaction(packetIn.getWindowId(), 0, false));
    return;
}
```

## Sending raw packets (e.g 1.9 interactions)
You can send raw packets with ViaMCP, you can use the following code to send raw packets:
```java
final PacketWrapper blockPlace = PacketWrapper.create(ServerboundPackets1_9.PLAYER_BLOCK_PLACEMENT, null); // Replace null with your stored UserConnection, see NetworkManager tutorial above
blockPlace.write(Type.POSITION1_8, new Position(0, 0, 0)); // Replace with the block position
blockPlace.write(Type.VAR_INT, 0); // Replace with the block face, see https://wiki.vg/index.php?title=Protocol&oldid=7617#Player_Digging
blockPlace.write(Type.VAR_INT, 0); // Replace with the hand, 0 for main hand, 1 for off hand
blockPlace.write(Type.UNSIGNED_BYTE, (short) 0); // The x pos of the crosshair, from 0 to 15 increasing from west to east
blockPlace.write(Type.UNSIGNED_BYTE, (short) 0); // The y pos of the crosshair, from 0 to 15 increasing from bottom to top
blockPlace.write(Type.UNSIGNED_BYTE, (short) 0); // The z pos of the crosshair, from 0 to 15 increasing from north to south

try {
    blockPlace.sendToServer(Protocol1_9To1_8.class); // Protocol class names are: server -> client version
} catch (Exception e) {
    // Packet sending failed
    throw new RuntimeException(e);
}
```

## Exporting Without JAR Files
This should fix most peoples issues with dependencies (usually NoClassDefFoundError or ClassNotFoundException)

- First export your client normally
- Open your client .jar file with an archive program (winrar or 7zip for example)
- Also open all libraries with the selected archive program (ViaVersion, ViaBackwards, ViaRewind and SnakeYaml)
- From ViaBackwards drag and drop ``assets`` and ``com`` folders to your client .jar
- From ViaRewind drag and drop ``assets`` and ``de`` folders to your client .jar
- From ViaSnakeYaml drag and drop ``org`` folder to your client .jar
- From ViaVersion drag and drop ``assets``, ``com`` and ``us`` folders to your client .jar 
- Then save and close, now your client should be working correctly ;)
