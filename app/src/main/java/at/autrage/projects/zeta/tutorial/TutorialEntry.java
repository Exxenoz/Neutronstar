package at.autrage.projects.zeta.tutorial;

public class TutorialEntry {
    public final int ArrowPositionX;
    public final int ArrowPositionY;
    public final int ArrowAlignmentX;
    public final int ArrowAlignmentY;
    public final boolean ArrowDirectionDown;
    public final boolean ArrowVisible;

    public final int TextResourceId;
    public final int TextPositionX;
    public final int TextPositionY;
    public final int TextBoxWidth;
    public final int TextAlignmentX;
    public final int TextAlignmentY;

    public TutorialEntry(int arrowPositionX, int arrowPositionY, int arrowAlignmentX, int arrowAlignmentY, boolean arrowDirectionDown, boolean arrowVisible,
                         int textResourceId, int textPositionX, int textPositionY, int textBoxWidth, int textAlignmentX, int textAlignmentY) {
        ArrowPositionX = arrowPositionX;
        ArrowPositionY = arrowPositionY;
        ArrowAlignmentX = arrowAlignmentX;
        ArrowAlignmentY = arrowAlignmentY;
        ArrowDirectionDown = arrowDirectionDown;
        ArrowVisible = arrowVisible;

        TextResourceId = textResourceId;
        TextPositionX = textPositionX;
        TextPositionY = textPositionY;
        TextBoxWidth = textBoxWidth;
        TextAlignmentX = textAlignmentX;
        TextAlignmentY = textAlignmentY;
    }
}
