import os
import subprocess
import sys
import re

def run_pytest():
    # Use the current directory (.) since we are inside UNIT TEST CODES
    test_dir = "."
    
    # Verify that the current directory contains .py files
    test_files = [f for f in os.listdir(test_dir) if f.endswith(".py")]
    if not test_files:
        print(f"No test files found in '{test_dir}'.")
        sys.exit(1)
    
    print(f"Found {len(test_files)} test files: {test_files}")
    
    # Construct the pytest command
    # Run pytest on the current directory with verbose output
    cmd = [sys.executable, "-m", "pytest", test_dir, "-v"]
    
    print(f"Running command: {' '.join(cmd)}")
    
    try:
        # Execute the pytest command and capture output
        result = subprocess.run(
            cmd,
            check=True,
            text=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE
        )
        # Print pytest output
        print("=== Pytest Output ===")
        print(result.stdout)
        if result.stderr:
            print("=== Errors ===")
            print(result.stderr)
        
        # Parse the summary line to extract pass/fail counts
        summary_line = None
        for line in result.stdout.splitlines():
            if "passed" in line and "in" in line:  # e.g., "45 passed, 5 failed in 5.67s"
                summary_line = line.strip()
                break
        
        passed_count = 0
        failed_count = 0
        if summary_line:
            # Use regex to extract passed and failed counts
            passed_match = re.search(r"(\d+)\s+passed", summary_line)
            failed_match = re.search(r"(\d+)\s+failed", summary_line)
            
            if passed_match:
                passed_count = int(passed_match.group(1))
            if failed_match:
                failed_count = int(failed_match.group(1))
        
        # Print conclusion
        print("=== Test Conclusion ===")
        print(f"Total Passed: {passed_count}")
        print(f"Total Failed: {failed_count}")
        print("All tests completed successfully." if failed_count == 0 else "Some tests failed. Check the output above for details.")
        return 0 if failed_count == 0 else 1
    
    except subprocess.CalledProcessError as e:
        print("=== Pytest Failed ===")
        print(e.stdout)
        print(e.stderr)
        
        # Parse the summary line even if pytest fails (e.g., due to test failures)
        summary_line = None
        for line in e.stdout.splitlines():
            if "passed" in line and "in" in line:
                summary_line = line.strip()
                break
        
        passed_count = 0
        failed_count = 0
        if summary_line:
            passed_match = re.search(r"(\d+)\s+passed", summary_line)
            failed_match = re.search(r"(\d+)\s+failed", summary_line)
            
            if passed_match:
                passed_count = int(passed_match.group(1))
            if failed_match:
                failed_count = int(failed_match.group(1))
        
        # Print conclusion
        print("=== Test Conclusion ===")
        print(f"Total Passed: {passed_count}")
        print(f"Total Failed: {failed_count}")
        print("Some tests failed. Check the output above for details.")
        return 1
    
    except Exception as e:
        print(f"Error running pytest: {str(e)}")
        return 1

if __name__ == "__main__":
    sys.exit(run_pytest())