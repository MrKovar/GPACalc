import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GPACalc 
{
	public static void main(String[] args) throws IOException
	{
		Scanner user_input = new Scanner(System.in);
		double current_gpa = 0;
		double total_letter_points = 0;
		double total_credits = 0;
		double overall_credits = 0;
		ArrayList<Double> values = new ArrayList<Double>();
		System.out.println("\t\tGPA Calculadora");
		System.out.println("\t\t By Cody Kovar");
		System.out.println("***********************************************");
		System.out.println();
		System.out.println("Would you like to:");
		System.out.println("1) Calculate GPA");
		System.out.println("2) Calculate How many A's to X GPA");
		System.out.print("Which would you like to do? ");
		int calculation = Integer.parseInt(user_input.nextLine());
		System.out.println();
		System.out.println("Would you like to:");
		System.out.println("1) Enter Manually");
		System.out.println("2) Read in from file");
		System.out.print("Which would you like to do? ");
		int input_source = Integer.parseInt(user_input.nextLine());
		System.out.println();
		// READING IN FROM KEYBOARD
		if(input_source == 1)
		{
			System.out.println("Please print values spaced as a Letter Grade a Space and the Credit amount");
			System.out.println("(e.g. \"A 3\" without the quotes)");
			System.out.println("and when finished with input, type the word \"finished\" without the quotes");
			String user_data = user_input.nextLine();
			while(!(user_data.equalsIgnoreCase("finished")))
			{
					String[] line = user_data.split(" ");
					if(line.length >= 2)
					{
						total_letter_points = getLetterValue(line);
						//System.out.println(total_letter_points);
						total_credits = Double.parseDouble(line[line.length - 1]);
						overall_credits += total_credits;
						values.add(total_letter_points * total_credits);
					}
				user_data = user_input.nextLine();
			}
			
			for(double d: values)
				current_gpa += d;
			current_gpa /= overall_credits;
			
			if(calculation == 1) // Reading in from keyboard and calculating GPA
			{
				current_gpa = roundGPA(current_gpa);
				System.out.println("Your current GPA is: " + current_gpa);
			}
			if(calculation == 2) // Reading in from keyboard and calculating TO GPA
			{
				double test_letter_points = 0;
				double test_gpa = current_gpa;
				for(double d: values)
					test_letter_points += d;
				double desired_gpa = 0;
				int required_a = 0;
				System.out.print("What is your desired GPA? ");
				desired_gpa = user_input.nextDouble();
				if(desired_gpa == 4 && current_gpa != 4) // Not able to get 4.0 without having one
				{
					System.out.println("It is impossible to get a 4.0 without retaking a course!");
				}
				else{ // If not requesting 4.0
					required_a = requiredAs(test_gpa, desired_gpa, test_letter_points,
							overall_credits);
					if(required_a > 0) 
					{
						test_gpa = roundGPA(test_gpa);
						System.out.println("Your GPA will be " + desired_gpa + " after " + required_a + " A's!");
					}
					else // If entered or calculated GPA is above or at desired GPA
					{
						System.out.println("Your GPA is already above or at " + desired_gpa + "!");
					}
				}
			}
			System.out.println("\n\tThank you for using my GPA Calculator");
			user_input.close();
		}
		// READING IN FROM FILE
		if(input_source == 2) 
		{
			System.out.println("Please have values spaced as a Letter Grade a Space and the Credit amount");
			System.out.println("(e.g. \"A 3\" without the quotes)");
			System.out.print("What is the file name? ");
			String file_name = user_input.nextLine();
			Scanner file_scan = new Scanner(new File(file_name));
			
			while(file_scan.hasNextLine())
			{
				String in_line = file_scan.nextLine();
				if(in_line.equalsIgnoreCase("finished"))
					break;
				if(in_line.length() > 2)
				{
					String[] line = in_line.split(" ");
					double a = getLetterValue(line);
					if(a != -1)
					{
						total_letter_points = a;
						total_credits = Double.parseDouble(line[line.length - 1]);
						overall_credits += total_credits;
						values.add(total_letter_points * total_credits);
					}
				}
			}
			for(double d: values)
				current_gpa += d;
			current_gpa /= overall_credits;
			if(calculation == 1) // Reading in from file and calculate GPA
			{
				current_gpa = roundGPA(current_gpa);
				System.out.println("Your current GPA is: " + current_gpa);
			}
			if(calculation == 2) // Reading in from file and calculate to GPA
			{
				double test_letter_points = 0;
				double test_gpa = current_gpa;
				for(double d: values)
					test_letter_points += d;
				double desired_gpa = 0;
				int required_a = 0;
				System.out.print("What is your desired GPA? ");
				desired_gpa = user_input.nextDouble();
				if(desired_gpa == 4 && current_gpa != 4)
				{
					System.out.println("It is impossible to get a 4.0 without retaking a course!");
				}
				else
				{
					required_a = requiredAs(test_gpa, desired_gpa, test_letter_points,
							overall_credits);
					if(required_a > 0)
					{
						test_gpa = roundGPA(test_gpa);
						System.out.println("Your GPA will be " + test_gpa + " after " + required_a + " A's!");
					}
					else
					{
						System.out.println("Your GPA is already above or at " + desired_gpa + "!");
					}
				}
			}
			System.out.println("\n\tThank you for using my GPA Calculator");
			file_scan.close();
			user_input.close();
		}

		
	}
	
	static double getLetterValue(String[] line)
	{
		switch (line[line.length - 2].toUpperCase())
		{
			case "A": return 4;
			case "A-": return 3.667;
			case "B+": return 3.333;
			case "B": return 3;
			case "B-": return 2.667;
			case "C+": return 2.333;
			case "C": return 2;
			case "C-": return 1.667;
			case "D+": return 1.333;
			case "D": return 1;
			case "D-": return 0.667;
			case "F": return 0;
			default:
			{
				//System.out.println("Found unreadable value at input: " + (i+1));
				return -1;
			}
		}
	}
	
	static double roundGPA(double current_gpa)
	{
		current_gpa *= 1000;
		current_gpa = (int)current_gpa;
		current_gpa /= 10;
		//current_gpa = Math.round(current_gpa);
		current_gpa /= 100;
		return current_gpa;
	}
	
	static int requiredAs(double t_gpa, double d_gpa, double tlp, double oc)
	{
		int req_a = 0;
		while(t_gpa < d_gpa)
		{
			tlp += 12.0;
			oc += 3;
			t_gpa = tlp / oc;
			req_a++;
		}
		return req_a;
	}
}

