package org.schabi.newpipe.extractor.stream;

import java.util.List;

public final class Frameset {

    private List<String> urls;
    private int frameWidth;
    private int frameHeight;
    private int totalCount;
    private int durationPerFrame;
    private int framesPerPageX;
    private int framesPerPageY;

    public Frameset(List<String> urls, int frameWidth, int frameHeight, int totalCount, int durationPerFrame, int framesPerPageX, int framesPerPageY) {
        this.urls = urls;
        this.totalCount = totalCount;
        this.durationPerFrame = durationPerFrame;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.framesPerPageX = framesPerPageX;
        this.framesPerPageY = framesPerPageY;
    }

    /**
     * @return list of urls to images with frames
     */
    public List<String> getUrls() {
        return urls;
    }

    /**
     * @return total count of frames
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @return maximum frames count by x
     */
    public int getFramesPerPageX() {
        return framesPerPageX;
    }

    /**
     * @return maximum frames count by y
     */
    public int getFramesPerPageY() {
        return framesPerPageY;
    }

    /**
     * @return width of a one frame, in pixels
     */
    public int getFrameWidth() {
        return frameWidth;
    }

    /**
     * @return height of a one frame, in pixels
     */
    public int getFrameHeight() {
        return frameHeight;
    }

    /**
     * @return duration per frame in milliseconds
     */
    public int getDurationPerFrame() {
        return durationPerFrame;
    }

    /**
     * Returns the information for the frame at stream position.
     *
     * @param position Position in milliseconds
     * @return An <code>int</code>-array containing the bounds and url where the indexes are specified as
     * followed:
     *
     * <ul>
     *     <li><code>0</code>: Index of the url</li>
     *     <li><code>1</code>: Left bound</li>
     *     <li><code>2</code>: Top bound</li>
     *     <li><code>3</code>: Right bound</li>
     *     <li><code>4</code>: Bottom bound</li>
     * </ul>
     */
    public int[] getFrameBoundsAt(long position) {
        if (position < 0 || position > ((totalCount + 1) * durationPerFrame)) {
            // Return the first frame as fallback
            return new int[] { 0, 0, 0, frameWidth, frameHeight };
        }

        final int framesPerStoryboard = framesPerPageX * framesPerPageY;
        final int absoluteFrameNumber = Math.min((int) (position / durationPerFrame), totalCount);

        final int relativeFrameNumber = absoluteFrameNumber % framesPerStoryboard;

        final int rowIndex = Math.floorDiv(relativeFrameNumber, framesPerPageX);
        final int columnIndex = relativeFrameNumber % framesPerPageY;

        return new int[] {
                /* storyboardIndex */ Math.floorDiv(absoluteFrameNumber, framesPerStoryboard),
                /* left */ columnIndex * frameWidth,
                /* top */ rowIndex * frameHeight,
                /* right */ columnIndex * frameWidth + frameWidth,
                /* bottom */ rowIndex * frameHeight + frameHeight };
    }
}