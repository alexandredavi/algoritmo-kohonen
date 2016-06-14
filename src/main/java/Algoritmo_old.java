
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Algoritmo_old {
	private static final int INPUT_NEURONS = 63;
	private static final int HIDDEN_NEURONS = 20;
	private static final int OUTPUT_NEURONS = 7;

	private static final double LEARN_RATE = 0.2;
	private static final int TRAINING_REPS = 400;

	private static double wih[][] = new double[INPUT_NEURONS + 1][HIDDEN_NEURONS];

	private static double who[][] = new double[HIDDEN_NEURONS + 1][OUTPUT_NEURONS];

	private static double inputs[] = new double[INPUT_NEURONS];
	private static double hidden[] = new double[HIDDEN_NEURONS];
	private static double target[] = new double[OUTPUT_NEURONS];
	private static double actual[] = new double[OUTPUT_NEURONS];

	private static double erro[] = new double[OUTPUT_NEURONS];
	private static double errh[] = new double[HIDDEN_NEURONS];

	private static final int MAX_SAMPLES = 21;

	private static int trainInputs[][] = new int[MAX_SAMPLES][INPUT_NEURONS];

	private static String PATH_TRAINING_FILES = "/home/alexandre/letras/";
	private static String PATH_TEST_FILES = "/home/alexandre/letras_teste/";

	private static final List<String> TRAINING_FILES = Arrays.asList(PATH_TRAINING_FILES + "a1.txt",
			PATH_TRAINING_FILES + "a2.txt", PATH_TRAINING_FILES + "a3.txt", PATH_TRAINING_FILES + "b1.txt",
			PATH_TRAINING_FILES + "b2.txt", PATH_TRAINING_FILES + "b3.txt", PATH_TRAINING_FILES + "c1.txt",
			PATH_TRAINING_FILES + "c2.txt", PATH_TRAINING_FILES + "c3.txt", PATH_TRAINING_FILES + "d1.txt",
			PATH_TRAINING_FILES + "d2.txt", PATH_TRAINING_FILES + "d3.txt", PATH_TRAINING_FILES + "e1.txt",
			PATH_TRAINING_FILES + "e2.txt", PATH_TRAINING_FILES + "e3.txt", PATH_TRAINING_FILES + "j1.txt",
			PATH_TRAINING_FILES + "j2.txt", PATH_TRAINING_FILES + "j3.txt", PATH_TRAINING_FILES + "k1.txt",
			PATH_TRAINING_FILES + "k2.txt", PATH_TRAINING_FILES + "k3.txt");

	private static final List<String> TESTING_FILES = Arrays.asList(PATH_TEST_FILES + "a1.txt",
			PATH_TEST_FILES + "a2.txt", PATH_TEST_FILES + "a3.txt", PATH_TEST_FILES + "b1.txt",
			PATH_TEST_FILES + "b2.txt", PATH_TEST_FILES + "b3.txt", PATH_TEST_FILES + "c1.txt",
			PATH_TEST_FILES + "c2.txt", PATH_TEST_FILES + "c3.txt", PATH_TEST_FILES + "d1.txt",
			PATH_TEST_FILES + "d2.txt", PATH_TEST_FILES + "d3.txt", PATH_TEST_FILES + "e1.txt",
			PATH_TEST_FILES + "e2.txt", PATH_TEST_FILES + "e3.txt", PATH_TEST_FILES + "j1.txt",
			PATH_TEST_FILES + "j2.txt", PATH_TEST_FILES + "j3.txt", PATH_TEST_FILES + "k1.txt",
			PATH_TEST_FILES + "k2.txt", PATH_TEST_FILES + "k3.txt");

	private static int trainOutput[][] = new int[][] { { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 1, 0 },
			{ 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 1 } };

	private final static Map<Integer, String> outputChar;
	static {
		outputChar = new HashMap<Integer, String>();
		outputChar.put(0, "A");
		outputChar.put(1, "B");
		outputChar.put(2, "C");
		outputChar.put(3, "D");
		outputChar.put(4, "E");
		outputChar.put(5, "J");
		outputChar.put(6, "K");

	}

	private static void NeuralNetwork() {
		int sample = 0;

		trainInputs = populateTrainInputs(TRAINING_FILES);

		assignRandomWeights();

		sample = trainNetwork(sample);

		getTrainingStats();

		System.out.println("\nTest network against original input:");
		testNetworkTraining();

		System.out.println("\nTest network against noisy input:");
		testNetworkWithNoise1();
	}

	private static int trainNetwork(int sample) {
		for (int epoch = 0; epoch < TRAINING_REPS; epoch++) {
			sample += 1;
			if (sample == MAX_SAMPLES) {
				sample = 0;
			}

			for (int i = 0; i < INPUT_NEURONS; i++) {
				inputs[i] = trainInputs[sample][i];
			}

			for (int i = 0; i < OUTPUT_NEURONS; i++) {
				target[i] = trainOutput[sample][i];
			}

			feedForward();

			backPropagate();

		}
		return sample;
	}

	private static int[][] populateTrainInputs(List<String> pathFiles) {
		int[][] inputsReturn = new int[MAX_SAMPLES][INPUT_NEURONS];
		for (int i = 0; i < pathFiles.size(); i++) {
			List<String> arquivo = LeitorDeArquivo.leArquivo(pathFiles.get(i));
			int positionToAdd = 0;
			for (String linha : arquivo) {
				for (int j = 0; j < linha.length(); j++) {
					char caracter = linha.charAt(j);
					if (caracter == '#') {
						inputsReturn[i][positionToAdd] = 1;
					} else if (caracter == '.') {
						inputsReturn[i][positionToAdd] = -1;
					} else {
						inputsReturn[i][positionToAdd] = 0;
					}
					positionToAdd++;
				}
			}
		}

		return inputsReturn;

	}

	private static void getTrainingStats() {
		double sum = 0.0;
		for (int i = 0; i < MAX_SAMPLES; i++) {
			for (int j = 0; j < INPUT_NEURONS; j++) {
				inputs[j] = trainInputs[i][j];
			}

			for (int j = 0; j < OUTPUT_NEURONS; j++) {
				target[j] = trainOutput[i][j];
			}

			feedForward();

			if (maximum(actual) == maximum(target)) {
				sum += 1;
			} else {
				System.out.println(inputs[0] + "\t" + inputs[1] + "\t" + inputs[2] + "\t" + inputs[3]);
				System.out.println(maximum(actual) + "\t" + maximum(target));
			}
		}

		System.out.println("Network is " + ((double) sum / (double) MAX_SAMPLES * 100.0) + "% correct.");

	}

	private static void testNetworkTraining() {
		// This function simply tests the training vectors against network.
		for (int i = 0; i < MAX_SAMPLES; i++) {
			for (int j = 0; j < INPUT_NEURONS; j++) {
				inputs[j] = trainInputs[i][j];
			}

			feedForward();

			System.out.print("Output: " + outputChar.get(maximum(actual)) + "\n");
		}
	}

	private static void testNetworkWithNoise1() {

		int[][] trainNoiseInputs = populateTrainInputs(TESTING_FILES);

		for (int i = 0; i < MAX_SAMPLES; i++) {
			for (int j = 0; j < INPUT_NEURONS; j++) {
				inputs[j] = trainNoiseInputs[i][j];
			}

			feedForward();

			System.out.print("Output: " + outputChar.get(maximum(actual)) + "\n");
		}

	}

	private static int maximum(final double[] vector) {
		// This function returns the index of the maximum of vector().
		int sel = 0;
		double max = vector[sel];

		for (int index = 0; index < OUTPUT_NEURONS; index++) {
			if (vector[index] > max) {
				max = vector[index];
				sel = index;
			}
		}
		return sel;
	}

	private static void feedForward() {
		double sum = 0.0;

		// Calculate input to hidden layer.
		for (int hid = 0; hid < HIDDEN_NEURONS; hid++) {
			sum = 0.0;
			for (int inp = 0; inp < INPUT_NEURONS; inp++) {
				sum += inputs[inp] * wih[inp][hid];
			}

			sum += wih[INPUT_NEURONS][hid];
			hidden[hid] = sigmoid(sum);
		}

		// Calculate the hidden to output layer.
		for (int out = 0; out < OUTPUT_NEURONS; out++) {
			sum = 0.0;
			for (int hid = 0; hid < HIDDEN_NEURONS; hid++) {
				sum += hidden[hid] * who[hid][out];
			}

			sum += who[HIDDEN_NEURONS][out]; // Add in bias.
			actual[out] = sigmoid(sum);
		}
	}

	private static void backPropagate() {
		// Calculate the output layer error (step 3 for output cell).
		for (int out = 0; out < OUTPUT_NEURONS; out++) {
			erro[out] = (target[out] - actual[out]) * sigmoidDerivative(actual[out]);
		}

		// Calculate the hidden layer error (step 3 for hidden cell).
		for (int hid = 0; hid < HIDDEN_NEURONS; hid++) {
			errh[hid] = 0.0;
			for (int out = 0; out < OUTPUT_NEURONS; out++) {
				errh[hid] += erro[out] * who[hid][out];
			}
			errh[hid] *= sigmoidDerivative(hidden[hid]);
		}

		// Update the weights for the output layer (step 4).
		for (int out = 0; out < OUTPUT_NEURONS; out++) {
			for (int hid = 0; hid < HIDDEN_NEURONS; hid++) {
				who[hid][out] += (LEARN_RATE * erro[out] * hidden[hid]);
			} // hid
			who[HIDDEN_NEURONS][out] += (LEARN_RATE * erro[out]); // Update the
																	// bias.
		} // out

		// Update the weights for the hidden layer (step 4).
		for (int hid = 0; hid < HIDDEN_NEURONS; hid++) {
			for (int inp = 0; inp < INPUT_NEURONS; inp++) {
				wih[inp][hid] += (LEARN_RATE * errh[hid] * inputs[inp]);
			} // inp
			wih[INPUT_NEURONS][hid] += (LEARN_RATE * errh[hid]);

		} // hid
	}

	private static void assignRandomWeights() {
		for (int inp = 0; inp <= INPUT_NEURONS; inp++) {
			for (int hid = 0; hid < HIDDEN_NEURONS; hid++) {
				// Assign a random weight value between -0.5 and 0.5
				wih[inp][hid] = new Random().nextDouble() - 0.5;
			} // hid
		} // inp

		for (int hid = 0; hid <= HIDDEN_NEURONS; hid++) // Do not subtract 1
														// here.
		{
			for (int out = 0; out < OUTPUT_NEURONS; out++) {
				// Assign a random weight value between -0.5 and 0.5
				who[hid][out] = new Random().nextDouble() - 0.5;
			} // out
		} // hid
	}

	private static double sigmoid(final double val) {
		return (1.0 / (1.0 + Math.exp(-val)));
	}

	private static double sigmoidDerivative(final double val) {
		return (val * (1.0 - val));
	}

	public static void main(String[] args) {
		NeuralNetwork();
	}

}
