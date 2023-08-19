package com.sevador.game.event.object;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.skills.woodcutting.TreeData.TreeDefinitions;
import com.sevador.game.node.model.skills.woodcutting.Woodcutting;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author Tyluur<tyluur@zandium.org>
 * 
 */
public class WoodcuttingActionEvent implements ObjectEvent {

	public boolean init() {
		EventManager.register(1281, this);// Oak
		EventManager.register(3037, this);// Oak
		EventManager.register(8462, this);// Oak
		EventManager.register(8463, this);// Oak
		EventManager.register(8464, this);// Oak
		EventManager.register(8465, this);// Oak
		EventManager.register(8466, this);// Oak
		EventManager.register(8467, this);// Oak
		EventManager.register(8473, this);// Diseased Oak
		EventManager.register(8474, this);// Diseased Oak
		EventManager.register(8475, this);// Diseased Oak
		EventManager.register(8476, this);// Diseased Oak
		EventManager.register(8477, this);// Dead Oak
		EventManager.register(8478, this);// Dead Oak
		EventManager.register(8479, this);// Dead Oak
		EventManager.register(8480, this);// Dead Oak
		EventManager.register(10083, this);// Oak
		EventManager.register(11999, this);// Oak
		EventManager.register(13156, this);// Oak drawers
		EventManager.register(13157, this);// Oak wardrobe
		EventManager.register(13285, this);// Oak chest
		EventManager.register(13286, this);// Oak chest
		EventManager.register(13294, this);// Oak table
		EventManager.register(13295, this);// Oak table
		EventManager.register(13301, this);// Oak bench
		EventManager.register(13302, this);// Carved oak bench
		EventManager.register(13384, this);// Oak prize chest
		EventManager.register(13385, this);// Oak prize chest
		EventManager.register(13578, this);// Oak table
		EventManager.register(13798, this);// Oak wall decoration
		EventManager.register(13799, this);// Oak wall decoration
		EventManager.register(13800, this);// Oak wall decoration
		EventManager.register(13801, this);// Oak wall decoration
		EventManager.register(13802, this);// Oak wall decoration
		EventManager.register(13803, this);// Oak wall decoration
		EventManager.register(13804, this);// Oak wall decoration
		EventManager.register(13805, this);// Oak wall decoration
		EventManager.register(13806, this);// Oak wall decoration
		EventManager.register(13807, this);// Oak wall decoration
		EventManager.register(13808, this);// Oak wall decoration
		EventManager.register(13809, this);// Oak wall decoration
		EventManager.register(13810, this);// Oak wall decoration
		EventManager.register(13811, this);// Oak wall decoration
		EventManager.register(13812, this);// Oak wall decoration
		EventManager.register(13813, this);// Oak wall decoration
		EventManager.register(15546, this);// Oak chair
		EventManager.register(17140, this);// Oaknock statue
		EventManager.register(17240, this);// Oaknock's machine
		EventManager.register(17241, this);// Oaknock's machine
		EventManager.register(17245, this);// Oaknock's Machine
		EventManager.register(17246, this);// Oaknock's Machine
		EventManager.register(17247, this);// Oaknock's Machine
		EventManager.register(17248, this);// Oaknock's exchanger
		EventManager.register(17249, this);// Oaknock's exchanger
		EventManager.register(18766, this);// Oak cape rack
		EventManager.register(37479, this);// Oak
		EventManager.register(38381, this);// Oak
		EventManager.register(38731, this);// Oak
		EventManager.register(38732, this);// Oak
		EventManager.register(38736, this);// Oak
		EventManager.register(38739, this);// Oak
		EventManager.register(38741, this);// Oak
		EventManager.register(38754, this);// Oak
		EventManager.register(39833, this);// Oak bench
		EventManager.register(39834, this);// Carved oak bench
		EventManager.register(44828, this);// Oak pet house
		EventManager.register(44834, this);// Oak pet feeder
		EventManager.register(51675, this);// Oak
		EventManager.register(241, this);// Strong tree
		EventManager.register(244, this);// Strong tree
		EventManager.register(250, this);// Strong tree
		EventManager.register(677, this);// Burning tree
		EventManager.register(678, this);// Tree
		EventManager.register(1276, this);// Tree
		EventManager.register(1277, this);// Tree
		EventManager.register(1278, this);// Tree
		EventManager.register(1280, this);// Tree
		EventManager.register(1282, this);// Dead tree
		EventManager.register(1283, this);// Dead tree
		EventManager.register(1284, this);// Dead tree
		EventManager.register(1285, this);// Dead tree
		EventManager.register(1286, this);// Dead tree
		EventManager.register(1287, this);// Dead tree
		EventManager.register(1288, this);// Dead tree
		EventManager.register(1289, this);// Dead tree
		EventManager.register(1291, this);// Dead tree
		EventManager.register(1292, this);// Dramen tree
		EventManager.register(1293, this);// Spirit tree
		EventManager.register(1294, this);// Spirit tree
		EventManager.register(1295, this);// Spirit tree
		EventManager.register(1297, this);// Fallen tree
		EventManager.register(1301, this);// Tree
		EventManager.register(1302, this);// Leafy tree
		EventManager.register(1303, this);// Tree
		EventManager.register(1304, this);// Tree
		EventManager.register(1305, this);// Tree
		EventManager.register(1306, this);// Magic tree
		EventManager.register(1307, this);// Maple tree
		EventManager.register(1317, this);// Spirit tree
		EventManager.register(1322, this);// Hollow tree
		EventManager.register(1326, this);// Tropical tree
		EventManager.register(1327, this);// Tropical tree
		EventManager.register(1328, this);// Tropical tree
		EventManager.register(1330, this);// Tree
		EventManager.register(1331, this);// Tree
		EventManager.register(1332, this);// Tree
		EventManager.register(1341, this);// Tree stump
		EventManager.register(1342, this);// Tree stump
		EventManager.register(1343, this);// Tree stump
		EventManager.register(1347, this);// Tree stump
		EventManager.register(1349, this);// Tree stump
		EventManager.register(1350, this);// Tree stump
		EventManager.register(1351, this);// Tree stump
		EventManager.register(1352, this);// Tree stump
		EventManager.register(1353, this);// Tree stump
		EventManager.register(1355, this);// Tree stump
		EventManager.register(1356, this);// Tree stump
		EventManager.register(1358, this);// Tree stump
		EventManager.register(1359, this);// Tree stump
		EventManager.register(1360, this);// Tropical tree
		EventManager.register(1361, this);// Tropical tree
		EventManager.register(1362, this);// Tropical tree
		EventManager.register(1365, this);// Dead tree
		EventManager.register(1371, this);// Tropical tree
		EventManager.register(1372, this);// Tropical tree
		EventManager.register(1373, this);// Tropical tree
		EventManager.register(1374, this);// Tropical tree
		EventManager.register(1375, this);// Tropical tree
		EventManager.register(1376, this);// Tropical tree
		EventManager.register(1377, this);// Tropical tree
		EventManager.register(1383, this);// Dead tree
		EventManager.register(1384, this);// Dead tree
		EventManager.register(1967, this);// Tree Door
		EventManager.register(1968, this);// Tree Door
		EventManager.register(2020, this);// Dead tree
		EventManager.register(2023, this);// Achey Tree
		EventManager.register(2073, this);// Banana Tree
		EventManager.register(2074, this);// Banana Tree
		EventManager.register(2075, this);// Banana Tree
		EventManager.register(2076, this);// Banana Tree
		EventManager.register(2077, this);// Banana Tree
		EventManager.register(2078, this);// Banana Tree
		EventManager.register(2237, this);// Palm tree
		EventManager.register(2289, this);// Hollow tree
		EventManager.register(2310, this);// Tree stump
		EventManager.register(2314, this);// Tree branch
		EventManager.register(2315, this);// Tree branch
		EventManager.register(2327, this);// Long branched tree
		EventManager.register(2409, this);// Tree
		EventManager.register(2410, this);// Tree
		EventManager.register(2411, this);// Tree
		EventManager.register(2447, this);// Tree
		EventManager.register(2448, this);// Tree
		EventManager.register(2577, this);// Palm tree
		EventManager.register(2578, this);// Palm tree
		EventManager.register(2652, this);// Tropical tree
		EventManager.register(2887, this);// Jungle Tree
		EventManager.register(2889, this);// Jungle Tree
		EventManager.register(2890, this);// Jungle Tree
		EventManager.register(2945, this);// Yommi tree baby
		EventManager.register(2946, this);// Yommi tree sapling
		EventManager.register(2948, this);// Adult Yommi tree
		EventManager.register(2950, this);// Felled Yommi tree
		EventManager.register(2953, this);// Rotten Yommi tree
		EventManager.register(2975, this);// Leafy Palm Tree
		EventManager.register(2976, this);// Leafy Palm Tree
		EventManager.register(3033, this);// Tree
		EventManager.register(3034, this);// Tree
		EventManager.register(3036, this);// Tree
		EventManager.register(3287, this);// Swamp tree
		EventManager.register(3293, this);// Swamp tree
		EventManager.register(3300, this);// Swamp tree
		EventManager.register(3358, this);// Swamp tree
		EventManager.register(3371, this);// Achey Tree stump
		EventManager.register(3514, this);// Rotting tree
		EventManager.register(3517, this);// Grotto tree
		EventManager.register(3879, this);// Tree
		EventManager.register(3880, this);// Tree stump
		EventManager.register(3881, this);// Tree
		EventManager.register(3882, this);// Tree
		EventManager.register(3883, this);// Tree
		EventManager.register(3884, this);// Tree stump
		EventManager.register(3885, this);// Tree
		EventManager.register(3886, this);// Tree
		EventManager.register(3887, this);// Tree
		EventManager.register(3888, this);// Tree
		EventManager.register(3889, this);// Tree
		EventManager.register(3890, this);// Tree
		EventManager.register(3891, this);// Tree
		EventManager.register(3892, this);// Fallen tree
		EventManager.register(3893, this);// Tree
		EventManager.register(3928, this);// Tree
		EventManager.register(3967, this);// Tree
		EventManager.register(3968, this);// Tree
		EventManager.register(4048, this);// Tree
		EventManager.register(4049, this);// Tree
		EventManager.register(4050, this);// Tree
		EventManager.register(4051, this);// Tree
		EventManager.register(4052, this);// Tree
		EventManager.register(4053, this);// Tree
		EventManager.register(4054, this);// Tree
		EventManager.register(4060, this);// Hollow tree
		EventManager.register(4061, this);// Tree stump
		EventManager.register(4142, this);// Swaying tree
		EventManager.register(4144, this);// Golden tree
		EventManager.register(4328, this);// Tree stump
		EventManager.register(4329, this);// Tree stump
		EventManager.register(4674, this);// Maple tree
		EventManager.register(4749, this);// Banana Tree
		EventManager.register(4750, this);// Banana Tree
		EventManager.register(4751, this);// Banana Tree
		EventManager.register(4752, this);// Banana Tree
		EventManager.register(4753, this);// Banana Tree
		EventManager.register(4754, this);// Banana Tree
		EventManager.register(4816, this);// Jungle Tree
		EventManager.register(4818, this);// Jungle Tree
		EventManager.register(4819, this);// Jungle Tree Stump
		EventManager.register(4820, this);// Jungle Tree
		EventManager.register(4821, this);// Jungle Tree Stump
		EventManager.register(4822, this);// Tree stump
		EventManager.register(4845, this);// Tropical tree
		EventManager.register(4846, this);// Tropical tree
		EventManager.register(4847, this);// Tropical tree
		EventManager.register(4849, this);// Tropical tree
		EventManager.register(4850, this);// Tropical tree
		EventManager.register(4851, this);// Tropical tree
		EventManager.register(5004, this);// Tree
		EventManager.register(5005, this);// Tree
		EventManager.register(5045, this);// Tree
		EventManager.register(5554, this);// Tree Stump
		EventManager.register(5848, this);// Tall tree
		EventManager.register(5904, this);// Tree
		EventManager.register(5947, this);// Dark hole under tree
		EventManager.register(6212, this);// Tree stump
		EventManager.register(6756, this);// Dead tree
		EventManager.register(61192, this);// Dead tree
		EventManager.register(38755, this);// Dead tree
		EventManager.register(7397, this);// Leafy tree
		EventManager.register(7398, this);// Leafy tree
		EventManager.register(7399, this);// Tree Stump
		EventManager.register(7400, this);// Tree Stump
		EventManager.register(7401, this);// Tree Stump
		EventManager.register(7402, this);// Tree Stump
		EventManager.register(7776, this);// Calquat Tree
		EventManager.register(7777, this);// Calquat Tree
		EventManager.register(7778, this);// Calquat Tree
		EventManager.register(7779, this);// Calquat Tree
		EventManager.register(7780, this);// Calquat Tree
		EventManager.register(7781, this);// Calquat Tree
		EventManager.register(7782, this);// Calquat Tree
		EventManager.register(7783, this);// Calquat Tree
		EventManager.register(7784, this);// Calquat Tree
		EventManager.register(7785, this);// Calquat Tree
		EventManager.register(7786, this);// Calquat Tree
		EventManager.register(7787, this);// Calquat Tree
		EventManager.register(7788, this);// Calquat Tree
		EventManager.register(7789, this);// Calquat Tree
		EventManager.register(7790, this);// Calquat Tree
		EventManager.register(7791, this);// Calquat Tree
		EventManager.register(7935, this);// Apple tree
		EventManager.register(7936, this);// Apple tree
		EventManager.register(7937, this);// Apple tree
		EventManager.register(7938, this);// Apple tree
		EventManager.register(7939, this);// Apple tree
		EventManager.register(7940, this);// Apple tree
		EventManager.register(7941, this);// Apple tree
		EventManager.register(7942, this);// Apple tree
		EventManager.register(7943, this);// Apple tree
		EventManager.register(7944, this);// Apple tree
		EventManager.register(7945, this);// Apple tree
		EventManager.register(7946, this);// Apple tree
		EventManager.register(7947, this);// Apple tree
		EventManager.register(7948, this);// Apple tree
		EventManager.register(7949, this);// Diseased apple tree
		EventManager.register(7950, this);// Diseased apple tree
		EventManager.register(7951, this);// Diseased apple tree
		EventManager.register(7952, this);// Diseased apple tree
		EventManager.register(7953, this);// Diseased apple tree
		EventManager.register(7954, this);// Diseased apple tree
		EventManager.register(7955, this);// Dead apple tree
		EventManager.register(7956, this);// Dead apple tree
		EventManager.register(7957, this);// Dead apple tree
		EventManager.register(7958, this);// Dead apple tree
		EventManager.register(7959, this);// Dead apple tree
		EventManager.register(7960, this);// Dead apple tree
		EventManager.register(7961, this);// Apple tree stump
		EventManager.register(7993, this);// Banana tree
		EventManager.register(7994, this);// Banana tree
		EventManager.register(7995, this);// Banana tree
		EventManager.register(7996, this);// Banana tree
		EventManager.register(7997, this);// Banana tree
		EventManager.register(7998, this);// Banana tree
		EventManager.register(7999, this);// Banana tree
		EventManager.register(8000, this);// Banana tree
		EventManager.register(8001, this);// Banana tree
		EventManager.register(8002, this);// Banana tree
		EventManager.register(8003, this);// Banana tree
		EventManager.register(8004, this);// Banana tree
		EventManager.register(8005, this);// Banana tree
		EventManager.register(8006, this);// Banana tree
		EventManager.register(8007, this);// Diseased banana tree
		EventManager.register(8008, this);// Diseased banana tree
		EventManager.register(8009, this);// Diseased banana tree
		EventManager.register(8010, this);// Diseased banana tree
		EventManager.register(8011, this);// Diseased banana tree
		EventManager.register(8012, this);// Diseased banana tree
		EventManager.register(8013, this);// Dead banana tree
		EventManager.register(8014, this);// Dead banana tree
		EventManager.register(8015, this);// Dead banana tree
		EventManager.register(8016, this);// Dead banana tree
		EventManager.register(8017, this);// Dead banana tree
		EventManager.register(8018, this);// Dead banana tree
		EventManager.register(8019, this);// Banana tree stump
		EventManager.register(8020, this);// Curry tree
		EventManager.register(8021, this);// Curry tree
		EventManager.register(8022, this);// Curry tree
		EventManager.register(8023, this);// Curry tree
		EventManager.register(8024, this);// Curry tree
		EventManager.register(8025, this);// Curry tree
		EventManager.register(8026, this);// Curry tree
		EventManager.register(8027, this);// Curry tree
		EventManager.register(8028, this);// Curry tree
		EventManager.register(8029, this);// Curry tree
		EventManager.register(8030, this);// Curry tree
		EventManager.register(8031, this);// Curry tree
		EventManager.register(8032, this);// Curry tree
		EventManager.register(8033, this);// Curry tree
		EventManager.register(8034, this);// Diseased curry tree
		EventManager.register(8035, this);// Diseased curry tree
		EventManager.register(8036, this);// Diseased curry tree
		EventManager.register(8037, this);// Diseased curry tree
		EventManager.register(8038, this);// Diseased curry tree
		EventManager.register(8039, this);// Diseased curry tree
		EventManager.register(8040, this);// Dead curry tree
		EventManager.register(8041, this);// Dead curry tree
		EventManager.register(8042, this);// Dead curry tree
		EventManager.register(8043, this);// Dead curry tree
		EventManager.register(8044, this);// Dead curry tree
		EventManager.register(8045, this);// Dead curry tree
		EventManager.register(8046, this);// Curry tree stump
		EventManager.register(8047, this);// Fruit Tree Patch
		EventManager.register(8048, this);// Fruit Tree Patch
		EventManager.register(8049, this);// Fruit Tree Patch
		EventManager.register(8050, this);// Fruit Tree Patch
		EventManager.register(8051, this);// Orange tree
		EventManager.register(8052, this);// Orange tree
		EventManager.register(8053, this);// Orange tree
		EventManager.register(8054, this);// Orange tree
		EventManager.register(8055, this);// Orange tree
		EventManager.register(8056, this);// Orange tree
		EventManager.register(8057, this);// Orange tree
		EventManager.register(8058, this);// Orange tree
		EventManager.register(8059, this);// Orange tree
		EventManager.register(8060, this);// Orange tree
		EventManager.register(8061, this);// Orange tree
		EventManager.register(8062, this);// Orange tree
		EventManager.register(8063, this);// Orange tree
		EventManager.register(8064, this);// Orange tree
		EventManager.register(8065, this);// Diseased orange tree
		EventManager.register(8066, this);// Diseased orange tree
		EventManager.register(8067, this);// Diseased orange tree
		EventManager.register(8068, this);// Diseased orange tree
		EventManager.register(8069, this);// Diseased orange tree
		EventManager.register(8070, this);// Diseased orange tree
		EventManager.register(8071, this);// Dead orange tree
		EventManager.register(8072, this);// Dead orange tree
		EventManager.register(8073, this);// Dead orange tree
		EventManager.register(8074, this);// Dead orange tree
		EventManager.register(8075, this);// Dead orange tree
		EventManager.register(8076, this);// Dead orange tree
		EventManager.register(8077, this);// Orange tree stump
		EventManager.register(8078, this);// Palm tree
		EventManager.register(8079, this);// Palm tree
		EventManager.register(8080, this);// Palm tree
		EventManager.register(8081, this);// Palm tree
		EventManager.register(8082, this);// Palm tree
		EventManager.register(8083, this);// Palm tree
		EventManager.register(8084, this);// Palm tree
		EventManager.register(8085, this);// Palm tree
		EventManager.register(8086, this);// Palm tree
		EventManager.register(8087, this);// Palm tree
		EventManager.register(8088, this);// Palm tree
		EventManager.register(8089, this);// Palm tree
		EventManager.register(8090, this);// Palm tree
		EventManager.register(8091, this);// Palm tree
		EventManager.register(8092, this);// Diseased palm tree
		EventManager.register(8093, this);// Diseased palm tree
		EventManager.register(8094, this);// Diseased palm tree
		EventManager.register(8095, this);// Diseased palm tree
		EventManager.register(8096, this);// Diseased palm tree
		EventManager.register(8097, this);// Diseased palm tree
		EventManager.register(8098, this);// Dead palm tree
		EventManager.register(8099, this);// Dead palm tree
		EventManager.register(8100, this);// Dead palm tree
		EventManager.register(8101, this);// Dead palm tree
		EventManager.register(8102, this);// Dead palm tree
		EventManager.register(8103, this);// Dead palm tree
		EventManager.register(8104, this);// Palm tree stump
		EventManager.register(8105, this);// Papaya tree
		EventManager.register(8106, this);// Papaya tree
		EventManager.register(8107, this);// Papaya tree
		EventManager.register(8108, this);// Papaya tree
		EventManager.register(8109, this);// Papaya tree
		EventManager.register(8110, this);// Papaya tree
		EventManager.register(8111, this);// Papaya tree
		EventManager.register(8112, this);// Papaya tree
		EventManager.register(8113, this);// Papaya tree
		EventManager.register(8114, this);// Papaya tree
		EventManager.register(8115, this);// Papaya tree
		EventManager.register(8116, this);// Papaya tree
		EventManager.register(8117, this);// Papaya tree
		EventManager.register(8118, this);// Papaya tree
		EventManager.register(8119, this);// Diseased papaya tree
		EventManager.register(8120, this);// Diseased papaya tree
		EventManager.register(8121, this);// Diseased papaya tree
		EventManager.register(8122, this);// Diseased papaya tree
		EventManager.register(8123, this);// Diseased papaya tree
		EventManager.register(8124, this);// Diseased papaya tree
		EventManager.register(8125, this);// Dead papaya tree
		EventManager.register(8126, this);// Dead papaya tree
		EventManager.register(8127, this);// Dead papaya tree
		EventManager.register(8128, this);// Dead papaya tree
		EventManager.register(8129, this);// Dead papaya tree
		EventManager.register(8130, this);// Dead papaya tree
		EventManager.register(8131, this);// Papaya tree stump
		EventManager.register(8339, this);// Spirit Tree Patch
		EventManager.register(8340, this);// Spirit Tree Patch
		EventManager.register(8341, this);// Spirit Tree Patch
		EventManager.register(8342, this);// Spirit Tree Patch
		EventManager.register(8343, this);// Spirit Tree
		EventManager.register(8344, this);// Spirit Tree
		EventManager.register(8345, this);// Spirit Tree
		EventManager.register(8346, this);// Spirit Tree
		EventManager.register(8347, this);// Spirit Tree
		EventManager.register(8348, this);// Spirit Tree
		EventManager.register(8349, this);// Spirit Tree
		EventManager.register(8350, this);// Spirit Tree
		EventManager.register(8351, this);// Spirit Tree
		EventManager.register(8352, this);// Spirit Tree
		EventManager.register(8353, this);// Spirit Tree
		EventManager.register(8354, this);// Spirit Tree
		EventManager.register(8355, this);// Spirit Tree
		EventManager.register(8356, this);// Spirit Tree
		EventManager.register(8358, this);// Diseased Spirit Tree
		EventManager.register(8359, this);// Diseased Spirit Tree
		EventManager.register(8360, this);// Diseased Spirit Tree
		EventManager.register(8361, this);// Diseased Spirit Tree
		EventManager.register(8362, this);// Diseased Spirit Tree
		EventManager.register(8363, this);// Diseased Spirit Tree
		EventManager.register(8364, this);// Diseased Spirit Tree
		EventManager.register(8365, this);// Diseased Spirit Tree
		EventManager.register(8366, this);// Diseased Spirit Tree
		EventManager.register(8367, this);// Diseased Spirit Tree
		EventManager.register(8368, this);// Diseased Spirit Tree
		EventManager.register(8370, this);// Dead Spirit Tree
		EventManager.register(8371, this);// Dead Spirit Tree
		EventManager.register(8372, this);// Dead Spirit Tree
		EventManager.register(8373, this);// Dead Spirit Tree
		EventManager.register(8374, this);// Dead Spirit Tree
		EventManager.register(8375, this);// Dead Spirit Tree
		EventManager.register(8376, this);// Dead Spirit Tree
		EventManager.register(8377, this);// Dead Spirit Tree
		EventManager.register(8378, this);// Dead Spirit Tree
		EventManager.register(8379, this);// Dead Spirit Tree
		EventManager.register(8380, this);// Dead Spirit Tree
		EventManager.register(8381, this);// Dead Spirit Tree
		EventManager.register(8392, this);// Tree patch
		EventManager.register(8393, this);// Tree patch
		EventManager.register(8394, this);// Tree patch
		EventManager.register(8395, this);// Tree patch
		EventManager.register(8396, this);// Magic Tree
		EventManager.register(8397, this);// Magic Tree
		EventManager.register(8398, this);// Magic Tree
		EventManager.register(8399, this);// Magic Tree
		EventManager.register(8400, this);// Magic Tree
		EventManager.register(8401, this);// Magic Tree
		EventManager.register(8402, this);// Magic Tree
		EventManager.register(8403, this);// Magic Tree
		EventManager.register(8404, this);// Magic Tree
		EventManager.register(8405, this);// Magic Tree
		EventManager.register(8406, this);// Magic Tree
		EventManager.register(8407, this);// Magic Tree
		EventManager.register(8408, this);// Magic Tree
		EventManager.register(8409, this);// Magic Tree
		EventManager.register(8410, this);// Magic Tree Stump
		EventManager.register(8411, this);// Diseased Magic Tree
		EventManager.register(8412, this);// Diseased Magic Tree
		EventManager.register(8413, this);// Diseased Magic Tree
		EventManager.register(8414, this);// Diseased Magic Tree
		EventManager.register(8415, this);// Diseased Magic Tree
		EventManager.register(8416, this);// Diseased Magic Tree
		EventManager.register(8417, this);// Diseased Magic Tree
		EventManager.register(8418, this);// Diseased Magic Tree
		EventManager.register(8419, this);// Diseased Magic Tree
		EventManager.register(8420, this);// Diseased Magic Tree
		EventManager.register(8421, this);// Diseased Magic Tree
		EventManager.register(8422, this);// Diseased Magic Tree
		EventManager.register(8423, this);// Dead Magic Tree
		EventManager.register(8424, this);// Dead Magic Tree
		EventManager.register(8425, this);// Dead Magic Tree
		EventManager.register(8426, this);// Dead Magic Tree
		EventManager.register(8427, this);// Dead Magic Tree
		EventManager.register(8428, this);// Dead Magic Tree
		EventManager.register(8429, this);// Dead Magic Tree
		EventManager.register(8430, this);// Dead Magic Tree
		EventManager.register(8431, this);// Dead Magic Tree
		EventManager.register(8432, this);// Dead Magic Tree
		EventManager.register(8433, this);// Dead Magic Tree
		EventManager.register(8434, this);// Dead Magic Tree
		EventManager.register(8435, this);// Maple Tree
		EventManager.register(8436, this);// Maple Tree
		EventManager.register(8437, this);// Maple Tree
		EventManager.register(8438, this);// Maple Tree
		EventManager.register(8439, this);// Maple Tree
		EventManager.register(8440, this);// Maple Tree
		EventManager.register(8441, this);// Maple Tree
		EventManager.register(8442, this);// Maple Tree
		EventManager.register(8443, this);// Maple Tree
		EventManager.register(8444, this);// Maple Tree
		EventManager.register(8445, this);// Maple tree stump
		EventManager.register(8468, this);// Oak tree stump
		EventManager.register(8481, this);// Willow Tree
		EventManager.register(8482, this);// Willow Tree
		EventManager.register(8483, this);// Willow Tree
		EventManager.register(8484, this);// Willow Tree
		EventManager.register(8485, this);// Willow Tree
		EventManager.register(8486, this);// Willow Tree
		EventManager.register(8487, this);// Willow Tree
		EventManager.register(8488, this);// Willow Tree
		EventManager.register(8489, this);// Willow tree stump
		EventManager.register(8503, this);// Yew tree
		EventManager.register(8504, this);// Yew tree
		EventManager.register(8505, this);// Yew tree
		EventManager.register(8506, this);// Yew tree
		EventManager.register(8507, this);// Yew tree
		EventManager.register(8508, this);// Yew tree
		EventManager.register(8509, this);// Yew tree
		EventManager.register(8510, this);// Yew tree
		EventManager.register(8511, this);// Yew tree
		EventManager.register(8512, this);// Yew tree
		EventManager.register(8513, this);// Yew tree
		EventManager.register(8514, this);// Yew tree stump
		EventManager.register(8708, this);// Holly tree
		EventManager.register(8709, this);// Hawthorn tree
		EventManager.register(8742, this);// Tree
		EventManager.register(8743, this);// Tree
		EventManager.register(8842, this);// Apple Tree
		EventManager.register(8843, this);// Apple Tree
		EventManager.register(8973, this);// Tree
		EventManager.register(8974, this);// Tree
		EventManager.register(9035, this);// Tree stump
		EventManager.register(9037, this);// Tree stump
		EventManager.register(9210, this);// White Tree patch
		EventManager.register(9211, this);// White Tree patch
		EventManager.register(9212, this);// White Tree patch
		EventManager.register(9213, this);// White Tree patch
		EventManager.register(9214, this);// White Tree patch
		EventManager.register(9215, this);// White Tree patch
		EventManager.register(9216, this);// White Tree patch
		EventManager.register(9217, this);// White Tree patch
		EventManager.register(9218, this);// White Tree patch
		EventManager.register(9219, this);// White Tree patch
		EventManager.register(9220, this);// White Tree patch
		EventManager.register(9221, this);// White Tree patch
		EventManager.register(9222, this);// White Tree patch
		EventManager.register(9263, this);// White Tree
		EventManager.register(9354, this);// Swamp tree
		EventManager.register(9355, this);// Swamp tree
		EventManager.register(9366, this);// Swamp tree
		EventManager.register(9385, this);// Swamp tree
		EventManager.register(9387, this);// Swamp tree
		EventManager.register(9388, this);// Swamp tree
		EventManager.register(9389, this);// Tree stump
		EventManager.register(9661, this);// Tree stump
		EventManager.register(9663, this);// Fallen tree
		EventManager.register(10081, this);// Tree
		EventManager.register(10082, this);// Tree
		EventManager.register(10139, this);// Palm tree
		EventManager.register(10140, this);// Palm tree
		EventManager.register(10141, this);// Palm tree
		EventManager.register(10951, this);// Tree stump
		EventManager.register(11059, this);// Tree stump
		EventManager.register(11112, this);// Dead tree
		EventManager.register(11394, this);// Young tree
		EventManager.register(11395, this);// Young tree
		EventManager.register(11434, this);// Evil tree
		EventManager.register(11435, this);// Evil tree
		EventManager.register(11436, this);// Evil tree
		EventManager.register(11437, this);// Evil oak tree
		EventManager.register(11438, this);// Evil oak tree
		EventManager.register(11439, this);// Evil oak tree
		EventManager.register(11440, this);// Evil willow tree
		EventManager.register(11441, this);// Evil willow tree
		EventManager.register(11442, this);// Evil willow tree
		EventManager.register(11443, this);// Evil maple tree
		EventManager.register(11444, this);// Evil maple tree
		EventManager.register(11855, this);// Tree stump
		EventManager.register(11862, this);// Tree stump
		EventManager.register(11864, this);// Tree stump
		EventManager.register(11865, this);// Swamp tree
		EventManager.register(11866, this);// Dead tree
		EventManager.register(11915, this);// Evil maple tree
		EventManager.register(11916, this);// Evil yew tree
		EventManager.register(11917, this);// Evil yew tree
		EventManager.register(11918, this);// Evil yew tree
		EventManager.register(11919, this);// Evil magic tree
		EventManager.register(11920, this);// Evil magic tree
		EventManager.register(11921, this);// Evil magic tree
		EventManager.register(11922, this);// Elder evil tree
		EventManager.register(11923, this);// Elder evil tree
		EventManager.register(11924, this);// Elder evil tree
		EventManager.register(12001, this);// Tree stump
		EventManager.register(12004, this);// Tree stump
		EventManager.register(12007, this);// Tree stump
		EventManager.register(12550, this);// Old tree
		EventManager.register(12551, this);// Old tree
		EventManager.register(12552, this);// Old tree
		EventManager.register(12553, this);// Old tree
		EventManager.register(12559, this);// Tree
		EventManager.register(12560, this);// Tree
		EventManager.register(12570, this);// Tropical tree
		EventManager.register(12571, this);// Tropical tree
		EventManager.register(12579, this);// Tropical tree
		EventManager.register(12580, this);// Tropical tree
		EventManager.register(12606, this);// Banana tree
		EventManager.register(12607, this);// Banana tree
		EventManager.register(12608, this);// Red Banana Tree
		EventManager.register(12609, this);// Red Banana Tree
		EventManager.register(12618, this);// Tropical tree
		EventManager.register(12627, this);// Tropical tree
		EventManager.register(12713, this);// Fallen tree
		EventManager.register(12714, this);// Fallen tree
		EventManager.register(12715, this);// Fallen tree
		EventManager.register(12732, this);// Dead tree
		EventManager.register(12733, this);// Tree stump
		EventManager.register(12894, this);// Tree Stump
		EventManager.register(12895, this);// Tree
		EventManager.register(13411, this);// Dead tree
		EventManager.register(13412, this);// Tree
		EventManager.register(13413, this);// Oak tree
		EventManager.register(13414, this);// Willow tree
		EventManager.register(13415, this);// Maple tree
		EventManager.register(13416, this);// Yew tree
		EventManager.register(13417, this);// Magic tree
		EventManager.register(13418, this);// Dead Tree
		EventManager.register(13419, this);// Tree
		EventManager.register(13420, this);// Oak Tree
		EventManager.register(13421, this);// Willow Tree
		EventManager.register(13422, this);// Yew tree
		EventManager.register(13423, this);// Maple tree
		EventManager.register(13424, this);// Magic tree
		EventManager.register(13843, this);// Swamp tree
		EventManager.register(13844, this);// Swamp tree
		EventManager.register(13845, this);// Swamp tree
		EventManager.register(13847, this);// Swamp tree
		EventManager.register(13848, this);// Swamp tree
		EventManager.register(13849, this);// Swamp tree
		EventManager.register(13850, this);// Swamp tree
		EventManager.register(14308, this);// Tree
		EventManager.register(14309, this);// Tree
		EventManager.register(14521, this);// Tree
		EventManager.register(14571, this);// Tree
		EventManager.register(14600, this);// Tree
		EventManager.register(14642, this);// Tree
		EventManager.register(14738, this);// Tree
		EventManager.register(14835, this);// Fallen tree
		EventManager.register(14836, this);// Fallen tree
		EventManager.register(14837, this);// Fallen tree
		EventManager.register(14838, this);// Fallen tree
		EventManager.register(14839, this);// Fallen tree
		EventManager.register(14840, this);// Fallen tree
		EventManager.register(14841, this);// Fallen tree
		EventManager.register(14842, this);// Fallen tree
		EventManager.register(14843, this);// Fallen tree
		EventManager.register(14844, this);// Fallen tree
		EventManager.register(14845, this);// Fallen tree
		EventManager.register(15362, this);// Big Tree space
		EventManager.register(15363, this);// Tree space
		EventManager.register(15488, this);// Tree trunk
		EventManager.register(15489, this);// Tree
		EventManager.register(15778, this);// Tropical tree
		EventManager.register(15779, this);// Tropical tree
		EventManager.register(15780, this);// Tropical tree
		EventManager.register(15781, this);// Tropical tree
		EventManager.register(15948, this);// Jungle Tree
		EventManager.register(15949, this);// Jungle Tree
		EventManager.register(15950, this);// Jungle Tree
		EventManager.register(15951, this);// Jungle Tree
		EventManager.register(15952, this);// Jungle Tree
		EventManager.register(15953, this);// Jungle Tree
		EventManager.register(15954, this);// Jungle Tree
		EventManager.register(15955, this);// Jungle Tree
		EventManager.register(15956, this);// Jungle Tree
		EventManager.register(15957, this);// Tropical tree
		EventManager.register(15958, this);// Tropical tree
		EventManager.register(15959, this);// Tropical tree
		EventManager.register(15961, this);// Bitternut Tree
		EventManager.register(15964, this);// Bitternut Tree
		EventManager.register(15965, this);// Bitternut Tree
		EventManager.register(15966, this);// Bitternut Tree
		EventManager.register(15967, this);// Bitternut Tree
		EventManager.register(15968, this);// Bitternut Tree
		EventManager.register(15969, this);// Bitternut Tree
		EventManager.register(15970, this);// Scrapey Tree
		EventManager.register(15971, this);// Scrapey Tree
		EventManager.register(16265, this);// Tree
		EventManager.register(16266, this);// Tree Stump
		EventManager.register(16604, this);// Dream tree
		EventManager.register(16605, this);// Dream Tree stump
		EventManager.register(16639, this);// Windswept Tree
		EventManager.register(17036, this);// Strong Tree
		EventManager.register(17037, this);// Strong Tree
		EventManager.register(17038, this);// Strong Tree
		EventManager.register(17056, this);// Crossbow Tree
		EventManager.register(17057, this);// Crossbow Tree
		EventManager.register(17058, this);// Crossbow Tree
		EventManager.register(17059, this);// Crossbow Tree
		EventManager.register(17060, this);// Crossbow Tree
		EventManager.register(17061, this);// Crossbow Tree
		EventManager.register(17062, this);// Crossbow Tree
		EventManager.register(17063, this);// Crossbow Tree
		EventManager.register(17074, this);// Strong Tree
		EventManager.register(17075, this);// Strong Tree
		EventManager.register(17076, this);// Strong Tree
		EventManager.register(17493, this);// Tree stump
		EventManager.register(17857, this);// Tree
		EventManager.register(17928, this);// Tree
		EventManager.register(18137, this);// Windswept tree
		EventManager.register(18836, this);// Tree branch
		EventManager.register(18862, this);// Tree
		EventManager.register(19038, this);// Wintumber tree
		EventManager.register(19093, this);// Tree
		EventManager.register(19165, this);// Tree
		EventManager.register(19166, this);// Tree
		EventManager.register(19167, this);// Tree
		EventManager.register(19172, this);// Tree
		EventManager.register(19370, this);// Tree
		EventManager.register(19446, this);// Tree
		EventManager.register(19650, this);// Young tree
		EventManager.register(19652, this);// Young tree
		EventManager.register(19653, this);// Young tree
		EventManager.register(19662, this);// Young tree
		EventManager.register(19663, this);// Young tree
		EventManager.register(19664, this);// Young tree
		EventManager.register(19670, this);// Young tree
		EventManager.register(19671, this);// Young tree
		EventManager.register(19672, this);// Young tree
		EventManager.register(19678, this);// Young tree
		EventManager.register(19679, this);// Young tree
		EventManager.register(19680, this);// Young tree
		EventManager.register(19731, this);// Tree
		EventManager.register(19755, this);// Tree
		EventManager.register(19765, this);// Tree
		EventManager.register(19773, this);// Tree
		EventManager.register(19795, this);// Tree
		EventManager.register(19801, this);// Tree
		EventManager.register(19815, this);// Tree
		EventManager.register(20711, this);// Sturdy tree
		EventManager.register(20715, this);// Sturdy tree
		EventManager.register(20750, this);// Dead tree
		EventManager.register(20751, this);// Dead tree
		EventManager.register(21274, this);// Tree Stump
		EventManager.register(21357, this);// Swamp tree
		EventManager.register(21426, this);// Slimy tree
		EventManager.register(21550, this);// Banana Tree
		EventManager.register(21766, this);// Sq'irk tree
		EventManager.register(21767, this);// Sq'irk tree
		EventManager.register(21768, this);// Sq'irk tree
		EventManager.register(21769, this);// Sq'irk tree
		EventManager.register(23054, this);// Tree stump
		EventManager.register(23381, this);// Dead tree
		EventManager.register(24168, this);// Dying tree
		EventManager.register(24169, this);// Dying tree Stump
		EventManager.register(25169, this);// Tree
		EventManager.register(25174, this);// Tree
		EventManager.register(25183, this);// Tree
		EventManager.register(25184, this);// Tree
		EventManager.register(25186, this);// Tree stump
		EventManager.register(25191, this);// A tropical tree
		EventManager.register(26200, this);// Tree stump
		EventManager.register(26201, this);// Tree
		EventManager.register(26203, this);// Tree stump
		EventManager.register(26204, this);// Tree
		EventManager.register(26713, this);// Warped tree roots
		EventManager.register(26714, this);// Warped tree roots
		EventManager.register(26715, this);// Warped tree roots
		EventManager.register(26716, this);// Parted tree roots
		EventManager.register(26717, this);// Parted tree roots
		EventManager.register(26718, this);// Warped tree roots
		EventManager.register(26719, this);// Parted tree roots
		EventManager.register(26720, this);// Spirit tree
		EventManager.register(26721, this);// Spirit tree
		EventManager.register(26722, this);// Spirit tree
		EventManager.register(26724, this);// Tree
		EventManager.register(26725, this);// Tree
		EventManager.register(27089, this);// Tree
		EventManager.register(27200, this);// Tree stump
		EventManager.register(27262, this);// Sullen tree
		EventManager.register(27263, this);// Bleak tree
		EventManager.register(27264, this);// Foul tree
		EventManager.register(27265, this);// Wretched tree
		EventManager.register(27341, this);// Mushroom tree
		EventManager.register(27342, this);// Mushroom tree
		EventManager.register(27343, this);// Mushroom tree
		EventManager.register(28455, this);// Slimy tree
		EventManager.register(28563, this);// Young tree
		EventManager.register(28564, this);// Young tree
		EventManager.register(28565, this);// Young tree
		EventManager.register(28951, this);// Eucalyptus tree
		EventManager.register(28952, this);// Eucalyptus tree
		EventManager.register(28953, this);// Eucalyptus tree
		EventManager.register(29088, this);// Achey tree
		EventManager.register(29089, this);// Achey tree
		EventManager.register(29090, this);// Achey tree
		EventManager.register(30795, this);// Tree
		EventManager.register(31057, this);// Tree stump
		EventManager.register(32294, this);// Dead tree
		EventManager.register(35970, this);// Tree branch
		EventManager.register(36510, this);// Tree stump
		EventManager.register(36765, this);// Tree
		EventManager.register(36891, this);// Tree stump
		EventManager.register(37334, this);// Dead tree
		EventManager.register(37368, this);// Tree
		EventManager.register(37393, this);// Tropical tree
		EventManager.register(37394, this);// Palm tree
		EventManager.register(37477, this);// Tree
		EventManager.register(37478, this);// Tree
		EventManager.register(37481, this);// Dead tree
		EventManager.register(37482, this);// Dead tree
		EventManager.register(37483, this);// Dead tree
		EventManager.register(37652, this);// Tree
		EventManager.register(37653, this);// Tree stump
		EventManager.register(37751, this);// Tree Stump
		EventManager.register(37821, this);// Cursed magic tree
		EventManager.register(37822, this);// Tree Stump
		EventManager.register(37823, this);// Magic tree
		EventManager.register(37824, this);// Tree Stump
		EventManager.register(38019, this);// Fruit tree patch
		EventManager.register(38051, this);// Avocado tree
		EventManager.register(38052, this);// Dead avocado tree
		EventManager.register(38053, this);// Lemon tree
		EventManager.register(38054, this);// Dead lemon tree
		EventManager.register(38055, this);// Mango tree
		EventManager.register(38056, this);// Dead mango tree
		EventManager.register(38057, this);// Pear tree
		EventManager.register(38058, this);// Dead pear tree
		EventManager.register(38059, this);// Plum tree
		EventManager.register(38060, this);// Dead plum tree
		EventManager.register(38061, this);// Quince tree
		EventManager.register(38062, this);// Dead quince tree
		EventManager.register(38379, this);// Tree
		EventManager.register(38380, this);// Tree
		EventManager.register(38382, this);// Dead tree
		EventManager.register(38383, this);// Dead tree
		EventManager.register(38640, this);// Tree stump (level 20 approx.)
		EventManager.register(38645, this);// Tree stump (level 20 approx.)
		EventManager.register(38689, this);// Gnarly tree
		EventManager.register(38760, this);// Tree
		EventManager.register(38782, this);// Tree
		EventManager.register(38783, this);// Tree
		EventManager.register(38784, this);// Tree
		EventManager.register(38785, this);// Tree
		EventManager.register(38786, this);// Tree
		EventManager.register(38787, this);// Tree
		EventManager.register(38788, this);// Tree
		EventManager.register(38789, this);// Tree
		EventManager.register(38791, this);// Tree
		EventManager.register(38792, this);// Tree
		EventManager.register(38795, this);// Tree
		EventManager.register(38805, this);// Tree
		EventManager.register(38842, this);// Tree
		EventManager.register(38867, this);// Tree stump
		EventManager.register(38868, this);// Tree stump
		EventManager.register(38869, this);// Tree stump
		EventManager.register(38870, this);// Tree
		EventManager.register(38871, this);// Tree stump
		EventManager.register(38872, this);// Tree
		EventManager.register(38874, this);// Tree
		EventManager.register(38875, this);// Tree
		EventManager.register(38877, this);// Tree stump
		EventManager.register(38878, this);// Tree
		EventManager.register(38879, this);// Tree stump
		EventManager.register(38880, this);// Tree
		EventManager.register(38881, this);// Tree
		EventManager.register(38883, this);// Tree
		EventManager.register(38884, this);// Tree
		EventManager.register(38886, this);// Tree
		EventManager.register(38887, this);// Tree
		EventManager.register(38888, this);// Tree
		EventManager.register(38889, this);// Tree
		EventManager.register(38892, this);// Tree
		EventManager.register(38895, this);// Tree
		EventManager.register(38896, this);// Tree
		EventManager.register(38897, this);// Tree
		EventManager.register(38898, this);// Tree
		EventManager.register(38899, this);// Tree stump
		EventManager.register(38900, this);// Tree
		EventManager.register(38909, this);// Tree stump
		EventManager.register(38910, this);// Tree
		EventManager.register(38911, this);// Tree
		EventManager.register(38912, this);// Tree
		EventManager.register(38914, this);// Tree
		EventManager.register(39090, this);// Tree
		EventManager.register(39097, this);// Tree
		EventManager.register(39100, this);// Tree
		EventManager.register(39119, this);// Tree
		EventManager.register(39121, this);// Tree
		EventManager.register(39136, this);// Tree
		EventManager.register(39137, this);// Tree
		EventManager.register(39138, this);// Tree
		EventManager.register(39145, this);// Tree
		EventManager.register(39154, this);// Tree
		EventManager.register(39166, this);// Tree
		EventManager.register(39168, this);// Tree
		EventManager.register(39328, this);// Tree
		EventManager.register(39430, this);// Tree
		EventManager.register(39586, this);// Tree (class 5)
		EventManager.register(39587, this);// Tree (class 4)
		EventManager.register(39588, this);// Tree (class 3)
		EventManager.register(39589, this);// Tree (class 2)
		EventManager.register(39590, this);// Tree (class 5)
		EventManager.register(39591, this);// Tree (class 4)
		EventManager.register(39592, this);// Tree (class 3)
		EventManager.register(39593, this);// Tree (class 2)
		EventManager.register(39594, this);// Tree (empty)
		EventManager.register(39595, this);// Tree (empty)
		EventManager.register(39596, this);// Tree (empty)
		EventManager.register(39597, this);// Tree (empty)
		EventManager.register(40196, this);// Tree
		EventManager.register(40294, this);// Tree
		EventManager.register(40295, this);// Tree
		EventManager.register(40296, this);// Tree
		EventManager.register(40297, this);// Tree
		EventManager.register(40298, this);// Tree
		EventManager.register(40299, this);// Tree
		EventManager.register(40300, this);// Tree
		EventManager.register(40301, this);// Tree
		EventManager.register(40302, this);// Tree
		EventManager.register(40303, this);// Tree
		EventManager.register(40312, this);// Tree
		EventManager.register(40313, this);// Tree
		EventManager.register(40318, this);// Tree
		EventManager.register(40320, this);// Tree
		EventManager.register(40345, this);// Tree
		EventManager.register(40346, this);// Tree
		EventManager.register(40347, this);// Tree
		EventManager.register(40348, this);// Tree
		EventManager.register(40349, this);// Tree
		EventManager.register(40350, this);// Tree stump
		EventManager.register(40351, this);// Tree stump
		EventManager.register(40352, this);// Tree stump
		EventManager.register(40353, this);// Tree stump
		EventManager.register(40354, this);// Tree stump
		EventManager.register(40355, this);// Tree stump
		EventManager.register(40356, this);// Tree stump
		EventManager.register(40357, this);// Tree stump
		EventManager.register(41695, this);// Slimy tree
		EventManager.register(41713, this);// Dead tree
		EventManager.register(41857, this);// Burning tree
		EventManager.register(41858, this);// Burning tree
		EventManager.register(41859, this);// Burning tree
		EventManager.register(41860, this);// Tree
		EventManager.register(41903, this);// Cinnamon tree
		EventManager.register(41904, this);// Sassafras tree
		EventManager.register(41905, this);// Ailanthus tree
		EventManager.register(41906, this);// Cedar tree
		EventManager.register(41907, this);// Mastic tree
		EventManager.register(42134, this);// Burning tree
		EventManager.register(42135, this);// Burning tree
		EventManager.register(42137, this);// A burnt tree
		EventManager.register(42303, this);// Slimy tree
		EventManager.register(42304, this);// Slimy tree
		EventManager.register(42860, this);// Tree stump
		EventManager.register(42893, this);// Dead tree covered in snow
		EventManager.register(43528, this);// Tree
		EventManager.register(43547, this);// Tree top
		EventManager.register(43548, this);// Tree
		EventManager.register(43549, this);// Tree top
		EventManager.register(43550, this);// Tree
		EventManager.register(43551, this);// Tree
		EventManager.register(43552, this);// Tree
		EventManager.register(45587, this);// Tree root
		EventManager.register(45588, this);// Tree root
		EventManager.register(45590, this);// Tree root
		EventManager.register(45591, this);// Tree root
		EventManager.register(45593, this);// Tree root
		EventManager.register(45594, this);// Tree root
		EventManager.register(46276, this);// Tree
		EventManager.register(46277, this);// Maple tree
		EventManager.register(46326, this);// Tree stump
		EventManager.register(46510, this);// Crystal tree
		EventManager.register(46757, this);// Dead tree
		EventManager.register(46758, this);// Dead tree
		EventManager.register(46759, this);// Dead tree
		EventManager.register(46760, this);// Dead tree
		EventManager.register(46761, this);// Dead tree
		EventManager.register(46762, this);// Dead tree
		EventManager.register(46763, this);// Dead tree
		EventManager.register(46764, this);// Dead tree
		EventManager.register(47594, this);// Dead tree
		EventManager.register(47595, this);// Tree stump
		EventManager.register(47596, this);// Dead tree
		EventManager.register(47597, this);// Tree stump
		EventManager.register(47598, this);// Dead tree
		EventManager.register(47599, this);// Tree stump
		EventManager.register(47600, this);// Dead tree
		EventManager.register(47601, this);// Tree stump
		EventManager.register(47748, this);// Christmas tree
		EventManager.register(47755, this);// Tree
		EventManager.register(49213, this);// Fallen tree
		EventManager.register(49220, this);// Spirit tree
		EventManager.register(49227, this);// Spirit tree
		EventManager.register(49705, this);// Tangle gum tree
		EventManager.register(49707, this);// Seeping elm tree
		EventManager.register(49709, this);// Blood spindle tree
		EventManager.register(49711, this);// Utuku tree
		EventManager.register(49713, this);// Spinebeam tree
		EventManager.register(49715, this);// Bovistrangler tree
		EventManager.register(49717, this);// Thigat tree
		EventManager.register(49719, this);// Corpsethorn tree
		EventManager.register(49721, this);// Entgallow tree
		EventManager.register(49723, this);// Grave creeper tree
		EventManager.register(49725, this);// Tangle gum tree
		EventManager.register(49727, this);// Seeping elm tree
		EventManager.register(49729, this);// Blood spindle tree
		EventManager.register(49731, this);// Utuku tree
		EventManager.register(49733, this);// Spinebeam tree
		EventManager.register(49735, this);// Bovistrangler tree
		EventManager.register(49737, this);// Thigat tree
		EventManager.register(49739, this);// Corpsethorn tree
		EventManager.register(49741, this);// Entgallow tree
		EventManager.register(49743, this);// Grave creeper tree
		EventManager.register(49745, this);// Tangle gum tree
		EventManager.register(49747, this);// Seeping elm tree
		EventManager.register(49749, this);// Blood spindle tree
		EventManager.register(49751, this);// Utuku tree
		EventManager.register(49753, this);// Spinebeam tree
		EventManager.register(49755, this);// Bovistrangler tree
		EventManager.register(49757, this);// Thigat tree
		EventManager.register(49759, this);// Corpsethorn tree
		EventManager.register(49761, this);// Entgallow tree
		EventManager.register(49763, this);// Grave creeper tree
		EventManager.register(49900, this);// Tree
		EventManager.register(51668, this);// Tree
		EventManager.register(51833, this);// Magic tree
		EventManager.register(51843, this);// Maple Tree
		EventManager.register(52612, this);// Mango tree
		EventManager.register(52617, this);// Fairy tree
		EventManager.register(52618, this);// Fairy tree
		EventManager.register(52619, this);// Fairy tree
		EventManager.register(52716, this);// Burning tree
		EventManager.register(52717, this);// Burning tree
		EventManager.register(52786, this);// Dead tree
		EventManager.register(53055, this);// Swamp tree
		EventManager.register(53065, this);// Swamp tree
		EventManager.register(53751, this);// Tangle gum tree
		EventManager.register(53753, this);// Seeping elm tree
		EventManager.register(53755, this);// Blood spindle tree
		EventManager.register(53757, this);// Utuku tree
		EventManager.register(53759, this);// Spinebeam tree
		EventManager.register(53761, this);// Bovistrangler tree
		EventManager.register(53763, this);// Thigat tree
		EventManager.register(53765, this);// Corpsethorn tree
		EventManager.register(53767, this);// Entgallow tree
		EventManager.register(53769, this);// Grave creeper tree
		EventManager.register(54755, this);// Tree
		EventManager.register(54766, this);// Tree Stump
		EventManager.register(54783, this);// Tree Stump
		EventManager.register(54801, this);// Swamp tree
		EventManager.register(55494, this);// Tangle gum tree
		EventManager.register(55496, this);// Seeping elm tree
		EventManager.register(55498, this);// Blood spindle tree
		EventManager.register(55500, this);// Utuku tree
		EventManager.register(55502, this);// Spinebeam tree
		EventManager.register(55504, this);// Bovistrangler tree
		EventManager.register(55506, this);// Thigat tree
		EventManager.register(55508, this);// Corpsethorn tree
		EventManager.register(55510, this);// Entgallow tree
		EventManager.register(55512, this);// Grave creeper tree
		EventManager.register(56933, this);// Christmas tree
		EventManager.register(57441, this);// Season Tree
		EventManager.register(57931, this);// Tree Stump
		EventManager.register(58108, this);// Swamp tree
		EventManager.register(58109, this);// Swamp tree
		EventManager.register(58121, this);// Swamp tree
		EventManager.register(58131, this);// Tree stump
		EventManager.register(58132, this);// Tree stump
		EventManager.register(58134, this);// Tree stump
		EventManager.register(58135, this);// Swamp tree
		EventManager.register(58140, this);// Swamp tree
		EventManager.register(58141, this);// Swamp tree
		EventManager.register(58142, this);// Swamp tree
		EventManager.register(59911, this);// Tree
		EventManager.register(59912, this);// Tree
		EventManager.register(59913, this);// Tree
		EventManager.register(59914, this);// Tree
		EventManager.register(59915, this);// Tree
		EventManager.register(59916, this);// Tree
		EventManager.register(59917, this);// Tree
		EventManager.register(59918, this);// Tree
		EventManager.register(59920, this);// Tree
		EventManager.register(61322, this);// Blisterwood Tree
		EventManager.register(61323, this);// Blisterwood Tree
		EventManager.register(61324, this);// Blisterwood Tree
		EventManager.register(61325, this);// Blisterwood Tree
		EventManager.register(61379, this);// Tree
		EventManager.register(61410, this);// Burnt tree
		EventManager.register(61411, this);// Tree stump
		EventManager.register(61464, this);// Burnt tree
		EventManager.register(61465, this);// Leafy tree
		EventManager.register(61527, this);// Tree
		EventManager.register(61528, this);// Tree
		EventManager.register(61532, this);// Tree
		EventManager.register(61533, this);// Tree
		EventManager.register(61537, this);// Tree
		EventManager.register(61538, this);// Tree
		EventManager.register(61542, this);// Tree
		EventManager.register(61543, this);// Tree
		EventManager.register(61550, this);// Overhanging tree
		EventManager.register(61551, this);// Snow-covered tree
		EventManager.register(61588, this);// Tree
		EventManager.register(61634, this);// Fallen tree
		EventManager.register(61900, this);// Tree
		EventManager.register(61901, this);// Tree
		EventManager.register(61902, this);// Tree
		EventManager.register(61903, this);// Tree
		EventManager.register(61904, this);// Tree
		return true;
	}

	public GameObject replacementTree(GameObject obj) {
		String name = obj.getDefinition().name.toLowerCase();
		Location loc = obj.getLocation();
		if (name.equalsIgnoreCase("Oak")) {
			return new GameObject(1356, loc);
		}
		if (name.equalsIgnoreCase("Tree")) {
			return new GameObject(1342, loc);
		}
		if (name.equalsIgnoreCase("Yew")) {
			return new GameObject(7402, loc);
		}
		if (name.contains("Maple")) {
			return new GameObject(1343, loc);
		}
		if (name.contains("Willow")) {
			return new GameObject(5554, loc);
		}
		if (name.equalsIgnoreCase("Magic tree")) {
			return new GameObject(7401, loc);
		}
		return new GameObject(1342, obj.getLocation());
	}

	@Override
	public boolean handle(final Player player, final GameObject object,
			OptionType type) {
		String name = object.getDefinition().name.toLowerCase();
		if (name.equals("tree")) {
			player.getSkillAction().setSkill(
					new Woodcutting(object, TreeDefinitions.NORMAL));
			return true;

		} else if (name.equals("dead tree")) {
			player.getSkillAction().setSkill(
					new Woodcutting(object, TreeDefinitions.DEAD));
			return true;
		} else if (name.equals("oak")) {
			player.getSkillAction().setSkill(
					new Woodcutting(object, TreeDefinitions.OAK));
			return true;
		} else if (name.equals("willow")) {
			player.getSkillAction().setSkill(
					new Woodcutting(object, TreeDefinitions.WILLOW));
			return true;
		} else if (name.equals("maple tree")) {
			player.getSkillAction().setSkill(
					new Woodcutting(object, TreeDefinitions.MAPLE));
			return true;
		} else if (name.equals("ivy")) {
			player.getSkillAction().setSkill(
					new Woodcutting(object, TreeDefinitions.IVY));
			return true;
		} else if (name.equals("yew")) {
			player.getSkillAction().setSkill(
					new Woodcutting(object, TreeDefinitions.YEW));
			return true;
		} else if (name.equals("magic tree")) {
			player.getSkillAction().setSkill(
					new Woodcutting(object, TreeDefinitions.MAGIC));
			return true;
		} else if (name.equals("cursed magic tree")) {
			player.getSkillAction().setSkill(
					new Woodcutting(object, TreeDefinitions.CURSED_MAGIC));
			return true;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.event.ObjectEvent#setDestination(org.argonite.game.
	 * node.model.player.Player,
	 * org.argonite.game.node.model.gameobject.GameObject)
	 */
	@Override
	public void setDestination(Player p, GameObject obj) {
		obj.getLocation();
	}

}
