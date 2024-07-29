
### Main-Class
Add this to the main class of your client (aka injection function)

```java
try {
    ViaMCP.create();
} catch (Exception e) {
    e.printStackTrace();
}
```


### Connection
You will need to modify 2 methods inside ``net.minecraft.network.Connection.java``

**1. Hook ViaVersion into the Netty Pipeline**

Find the method, that is ``m_264299_``, ``configureSerialization``

Find the vanilla network pipeline call:
```java
p_265436_.addLast("splitter", new Varint21FrameDecoder(p_299297_)).addLast("decoder", new PacketDecoder(attributekey)).addLast("prepender", new Varint21LengthFieldPrepender()).addLast("encoder", new PacketEncoder(attributekey1)).addLast("unbundler", new PacketBundleUnpacker(attributekey1)).addLast("bundler", new PacketBundlePacker(attributekey));
```

After the vanilla network pipeline call, add the ViaMCP protocol pipeline hook:
```java
if(p_265436_.channel() instanceof SocketChannel && ViaLoadingBase.getInstance().getTargetVersion().getVersion() != ViaMCP.NATIVE_VERSION) {
    final UserConnection user = new UserConnectionImpl(p_265436_.channel(), p_265104_ == PacketFlow.CLIENTBOUND);
    new ProtocolPipelineImpl(user);

    p_265436_.addLast(new MCPVLBPipeline(user));
}
```

**2. Fix the compression in the net.minecraft.network.Connection#setupCompression function**

Simply call the following code at the end of the method in Minecraft:
```java
this.channel.pipeline().fireUserEventTriggered(new CompressionReorderEvent());
```

## Version Control
You will need to add a button to access the protocol switcher ~~(or alternatively use the version slider under this section)~~ <br>
In ``createNormalMenuOptions()`` function add (if in TitleScreen):


add this to the bottom of the method:
```java
this.addRenderableWidget(Button.builder(Component.text("Version"), (button) -> {
    assert this.minecraft != null;
    this.minecraft.setScreen(new GuiProtocolSelector(Minecraft.getInstance().screen));
}).bounds(5, 5, 90, 20).build());
```