import java.io.IOException;
import java.util.Scanner;

public class bbst {
	public static void main(String[] args) throws IOException {
		RBTree<Integer> tree = new RBTree<Integer>();
		int theID, m, ID1, ID2;
		String a = args[0];
		tree.Initialization(tree, a);
		Scanner input = new Scanner(System.in);
		String line;
			while ((line = input.nextLine()) != null) {
				String[] command = line.split(" ");
				switch (command[0]) {
				case "increase":
					theID = Integer.parseInt(command[1]);
					m = Integer.parseInt(command[2]);
					tree.Increase(tree, theID, m);
					break;
				case "reduce":
					theID = Integer.parseInt(command[1]);
					m = Integer.parseInt(command[2]);
					tree.Reduce(tree, theID, m);
					break;
				case "count":
					theID = Integer.parseInt(command[1]);
					tree.Count(tree, theID);
					break;
				case "inrange":
					ID1 = Integer.parseInt(command[1]);
					ID2 = Integer.parseInt(command[2]);
					tree.InRange(tree, ID1, ID2);
					break;
				case "next":
					theID = Integer.parseInt(command[1]);
					tree.Next(tree, theID);
					break;
				case "previous":
					theID = Integer.parseInt(command[1]);
					tree.Previous(tree, theID);
					break;
				case "quit":
					tree.clear();
					System.exit(0);
					break;
				}
			}
		}
	}