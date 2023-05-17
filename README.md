# ViaMCP
ViaVersion VersionSwitcher for Minecraft Coder Pack (MCP)

## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/FlorianMichael/ViaMCP/issues).  
If you just want to talk or need help with ViaMCP feel free to join my
[Discord](https://discord.gg/BwWhCHUKDf).

## Setup
Firstly, you will need to add the listed libraries into your dependencies in IntelliJ or Eclipse

Dependencies (Included inside ``libraries`` folder)
```
ViaVersion-[ver].jar > ViaVersion > https://github.com/ViaVersion/ViaVersion
ViaBackwards-[ver].jar > ViaBackwards > https://github.com/ViaVersion/ViaBackwards
ViaRewind-[ver].jar > ViaRewind > https://github.com/ViaVersion/ViaRewind
snakeyml-2.0.jar > SnakeYaml > https://bitbucket.org/snakeyaml/snakeyaml
```

Secondly, you need to add code that allows you to actually use ViaMCP (**Choose the version folder that corresponds with your client version**)

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
You will need to change 2 functions in NetworkManager.java <br>
**1. Hook ViaVersion into the Netty Pipeline**

Name should be ``func_181124_a``, ``createNetworkManagerAndConnect`` or contain ``(Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)lazyloadbase.getValue())``

After:

(1.8.x)

```java
p_initChannel_1_.pipeline().addLast((String)"timeout", (ChannelHandler)(new ReadTimeoutHandler(30))).addLast((String)"splitter", (ChannelHandler)(new MessageDeserializer2())).addLast((String)"decoder", (ChannelHandler)(new MessageDeserializer(EnumPacketDirection.CLIENTBOUND))).addLast((String)"prepender", (ChannelHandler)(new MessageSerializer2())).addLast((String)"encoder", (ChannelHandler)(new MessageSerializer(EnumPacketDirection.SERVERBOUND))).addLast((String)"packet_handler", (ChannelHandler)networkmanager);
```

(1.12.x)

```java
p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new NettyVarint21FrameDecoder()).addLast("decoder", new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", new NettyVarint21FrameEncoder()).addLast("encoder", new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", networkmanager);
```

Add:

```java
if (p_initChannel_1_ instanceof SocketChannel && ViaLoadingBase.getInstance().getTargetVersion() != ViaMCP.NATIVE_VERSION) {
    final UserConnection user = new UserConnectionImpl(p_initChannel_1_, true);
    new ProtocolPipelineImpl(user);
    
    p_initChannel_1_.pipeline().addLast(new MCPVLBPipeline(user));
}
```
Which should look like this afterwards (1.8.x for example):

```java
p_initChannel_1_.pipeline().addLast((String)"timeout", (ChannelHandler)(new ReadTimeoutHandler(30))).addLast((String)"splitter", (ChannelHandler)(new MessageDeserializer2())).addLast((String)"decoder", (ChannelHandler)(new MessageDeserializer(EnumPacketDirection.CLIENTBOUND))).addLast((String)"prepender", (ChannelHandler)(new MessageSerializer2())).addLast((String)"encoder", (ChannelHandler)(new MessageSerializer(EnumPacketDirection.SERVERBOUND))).addLast((String)"packet_handler", (ChannelHandler)networkmanager);

if (p_initChannel_1_ instanceof SocketChannel && ViaLoadingBase.getInstance().getTargetVersion() != ViaMCP.NATIVE_VERSION) {
    final UserConnection user = new UserConnectionImpl(p_initChannel_1_, true);
    new ProtocolPipelineImpl(user);
    
    p_initChannel_1_.pipeline().addLast(new MCPVLBPipeline(user));
}
```

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
this.buttonList.add(ViaMCP.getInstance().asyncSlider);
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