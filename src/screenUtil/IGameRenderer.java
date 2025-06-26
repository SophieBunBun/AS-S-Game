package screenUtil;

public interface IGameRenderer {

    void initialize(ScreenController screenController);
    void setOpenScreen(ScreenType type);
    void close();
    void render();
}
