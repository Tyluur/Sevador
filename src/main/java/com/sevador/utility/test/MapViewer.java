package com.sevador.utility.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.sevador.utility.MapData;

/**
 * A tool viewing the world map.
 * @author Emperor
 *
 */
public class MapViewer extends JFrame implements MouseWheelListener, MouseListener, MouseMotionListener {
	
	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 4904174589867234201L;

	/**
	 * The mapdata path.
	 */
	private static final String PATH = "./data/mapdata/viewer img/";
	
	/**
	 * The mouse x-coordinate.
	 */
	private int mouseX = -1;
	
	/**
	 * The mouse y-coordinate.
	 */
	private int mouseY = -1;
	
	/**
	 * The height level.
	 */
	private int plane = 1;
	
	/**
	 * The current x coordinate.
	 */
	private int currentX = 3222;
	
	/**
	 * The current y coordinate.
	 */
	private int currentY = 3217;
	
	/**
	 * The current zoom.
	 */
	private int zoom = 100;
	
	/**
	 * The color mapping.
	 */
	private static final Map<Integer, Image> MAP = new HashMap<Integer, Image>();
	
	/**
	 * The main method.
	 * @param args The arguments.
	 * @throws Throwable When an exception occurs.
	 */
	public static void main(String... args) throws Throwable {
		System.out.println("MapData size: " + MapData.getMapData().size() + ", " + (new Color(0) == Color.BLACK) + ", " + Color.GREEN.getRGB());
		MapViewer viewer = new MapViewer();
		if (args.length > 55) {
			viewer.plane = Integer.parseInt(args[0]);
		}
		viewer.init();
	}

	/**
	 * Constructs a new {@code MapViewer} {@code Object}.
	 */
	private MapViewer() {
		super("Map viewer");
		super.addMouseWheelListener(this);
		super.addMouseListener(this);
		super.addMouseMotionListener(this);
		super.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
			    if (getWidth() != getHeight()) {
			    	int size = getWidth() > getHeight() ? getWidth() : getHeight();
			    	setSize(size, size);
			    }
			}
		});
		setSize(600, 600);
		JFrame tools = new JFrame();
		tools.setLayout(new GridLayout());
		JTextField xCoordinate = new JTextField("X-coordinate");
		tools.add(xCoordinate);
		JTextField yCoordinate = new JTextField("Y-coordinate");
		tools.add(yCoordinate);
		JTextField height = new JTextField("Height");
		tools.add(height);
		tools.setSize(500, 300);
		tools.setVisible(true);
	}
		
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		int sizeX = (int) (getWidth() / (zoom / 100D)) >> 1;
		int sizeY = (int) (getHeight() / (zoom / 100D)) >> 1;
		g.fillRect(0, 0, getWidth(), getHeight());
		int baseX = (currentX - sizeX) >> 6;
		int baseY = (currentY - sizeY) >> 6;
		int width = (getWidth() >> 3) << 3;
		int height = (getHeight() >> 3) << 3;
		for (int x = baseX; x < (currentX + sizeX + 1) >> 6; x++) {
			for (int y = baseY; y < (currentY + sizeY + 1) >> 6; y++) {
				Image img = MAP.get(getHash(x << 6, y << 6, plane));
				if (img == null) {
					continue;
				}
				img = getResized(img);
				int destX = (x - baseX) * img.getWidth(null);
				int destY = (y - baseY) * img.getHeight(this);
				g.drawImage(img,  destX, width - destY, destX + img.getWidth(null), height - (destY + img.getHeight(null)),
			             0, 0, img.getWidth(null), img.getHeight(null), this);
				//System.out.println("Image width: " + img.getWidth(this));
			}
		}
		g.setColor(Color.BLUE);
		g.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		g.drawString(new StringBuilder("Zoom: ").append(zoom).append("%.").toString(), 20, 50);
		//g.setColor(Color.RED);
		//TODO:g.fillRect();
		System.out.println("Size x: " + sizeX + ", size y: " + sizeY);
	}
	
	/**
	 * Gets this image resized, if necassery.
	 * @param img The image.
	 * @return The resized image.
	 */
	private Image getResized(Image img) {
        int w = img.getWidth(this);  
        int h = img.getHeight(this); 
        int newW = (int) (w * (zoom / 100D));
        int newH = (int) (h * (zoom / 100D));
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);  
        Graphics2D g = dimg.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;  
	}

	/**
	 * Initializes the mapviewer.
	 */
	private void init() throws Throwable {
		
		/*MapData.init();
		Runnable r = createImageMap(0, MapData.getMapData().keySet().size() >> 1);
		Runnable r1 = createImageMap(MapData.getMapData().keySet().size() >> 1, MapData.getMapData().keySet().size());
		ParallelExecutor exec = new ParallelExecutor();
		exec.offer(r, r1);*/
		MapData.init();
		Runnable r = load(0, MapData.getMapData().keySet().size());
		r.run();/*Runnable r1 = load(MapData.getMapData().keySet().size() >> 1, MapData.getMapData().keySet().size());
		ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		service.execute(r);
		service.execute(r1);*/
	}
	
	/**
	 * If a loading thread has finished its work.
	 */
	@SuppressWarnings("unused")
	private static boolean completed = false;
	
	/**
	 * Loads the mapdata image files.
	 * @param start The start index.
	 * @param end The end index.
	 * @return The runnable to execute.
	 */
	public Runnable load(final int start, final int end) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				DataOutputStream dos = null;
				try {
					dos = new DataOutputStream(new FileOutputStream(PATH + "XteaKeys.bin"));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					return;
				}
				Object[] keys = MapData.getMapData().keySet().toArray();
				for (int i = start; i < end; i++) {
					Integer key = (Integer) keys[i];
					if (key == null) {
						continue;
					}
					int rx = key >> 8;
					int ry = key & 0xFF;
					File f = new File(new StringBuilder("./data/mapdata/viewer img/").append("m").append(rx).append("_").append(ry).append("_").append(plane).append(".png").toString());
					if (!f.exists()) {
						continue;
					}
					try {
						dos.writeInt(key);
						BufferedImage img = ImageIO.read(f);
						MAP.put(getHash(rx << 6, ry << 6, plane), img);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				try {
					dos.writeInt(-1);
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " is done.");
				setVisible(true);
				completed = true;
			}			
		};
		return r;
	}
	
	/**
	 * Loads the mapdata images.
	 * @param startX The start x-coordinate.
	 * @param startY The start y-coordinate.
	 * @param endX The end x-coordinate.
	 * @param endY The end y-coordinate.
	 * @return The runnable.
	 */
	public Runnable load(final int startX, final int startY, final int endX, final int endY) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				List<Integer> loaded = new ArrayList<Integer>();
				for (int x = startX; x < endX; x++) {
					for (int y = startY; y < endY; y++) {
						int regionX = x >> 6;
						int regionY = y >> 6;
						int regionId = regionY | regionX << 8;
						if (!loaded.contains(regionId)) {
							loaded.add(regionId);
							File mapregion = new File(new StringBuilder(PATH).append("m").append(regionX).append("_")
													.append(regionY).append("_").append(plane).append(".png").toString());
							if (!mapregion.exists()) {
								continue;
							}
							try {
								BufferedImage img = ImageIO.read(mapregion);
								MAP.put(getHash(regionX << 6, regionY << 6, plane), img);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				}
				setVisible(true);
			}			
		};
		return r;
	}
	
	/**
	 * Gets the hash for a location.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @return The hash.
	 */
	public int getHash(int x, int y, int z) {
		return x << 14 | y & 0x3fff | z << 28;
	}
	
	/**
	 * Returns a runnable that converts mapregion data to images.
	 * @param start The start index.
	 * @param end The end index.
	 * @return The runnable.
	 */
	public Runnable createImageMap(final int start, final int end) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				Object[] keys = MapData.getMapData().keySet().toArray();
				for (int i = start; i < end; i++) {
					Integer key = (Integer) keys[i];
					if (key == null) {
						continue;
					}
					int rx = key >> 8;
					int ry = key & 0xFF;
					boolean contin = false;
					for (int z = 0; z < 4; z++) {
						File f = new File(new StringBuilder(PATH).append("m").append(rx).append("_").append(ry).append("_").append(z).append(".png").toString());
						if (f.exists()) {
							contin = true;
						}
					}
					if (contin) {
						continue;
					}
					File f = new File(new StringBuilder("./data/mapdata/viewer p/").append("m").append(rx).append("_").append(ry).append(".dat").toString());
					if (!f.exists()) {
						continue;
					}
					try {
						DataInputStream in = new DataInputStream(new FileInputStream(f));
						int baseX = rx << 6;
						int baseY = ry << 6;
						int[][][] map = new int[4][64][64];
						while (in.available() > 0) {
							int hash = in.readInt();
							int x = (hash >> 14 & 0xFFF) - baseX;
							int y = (hash & 0xFFF) - baseY;
							int z = (hash >> 28) % 4;
							if (x < 0 || y < 0 || z < 0 || x > 63 || y > 63) {
								continue;
							}
							map[z][x][y] = in.readInt();
							in.close();
						}
						for (int z = 0; z < 4; z++) {
							BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
							Graphics2D g = img.createGraphics();
							g.setColor(Color.GREEN);
							g.fillRect(0, 0, 64, 64);
							for (int x = 0; x < 64; x++) {
								for (int y = 0; y < 64; y++) {
									int color = map[z][x][y];
									if (color != 0) {
										g.setColor(new Color(color));
										g.fillRect(x, y, 1, 1);
									}
								}
							}
							ImageIO.write(img, "png", new File(new StringBuilder(PATH).append("m").append(rx).append("_").append(ry).append("_").append(z).append(".png").toString()));
						}
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
				System.out.println(Thread.currentThread().getName() + " is done.");
			}			
		};
		return r;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int lastZoom = zoom;
		zoom += e.getWheelRotation() * 10;
		if (zoom < 20) {
			zoom = 20;
		} else if (zoom > 200) {
			zoom = 200;
		}
		if (zoom != lastZoom) {
			paint(getGraphics());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		currentX += -((e.getX() - mouseX) / (zoom / 100D));
		currentY += (e.getY() - mouseY) / (zoom / 100D);
		if (currentX < getWidth() >> 1) {
			currentX = getWidth() >> 1;
		} else if (currentX > 18000) {
			currentX = 18000;
		}
		if (currentY < getHeight() >> 1) {
			currentY = getHeight() >> 1;
		} else if (currentY > 18000) {
			currentY = 18000;
		}
		paint(getGraphics());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
}