# Run GitHub Actions Manually

Follow these steps to run the Selenium tests manually from GitHub Actions.

## Steps

1. Open the repository:

   ```text
   https://github.com/Shubham00117/selenium-docker
   ```

2. Click the **Actions** tab.

3. In the left sidebar, select **Selenium Tests**.

4. Click **Run workflow**.

5. Keep the branch as **main**.

6. Click the green **Run workflow** button.

7. Wait for the workflow run to complete.

8. Open the latest workflow run to see the test status.

9. Download the report:

   - Open the completed workflow run.
   - Scroll down to **Artifacts**.
   - Download **selenium-test-output**.
   - Extract the zip file.
   - Open the Extent report from:

     ```text
     extent-reports/
     ```

## Workflow Details

The manual workflow run does the following:

- Starts Selenium Grid using Docker Compose
- Waits for Selenium Grid to become ready
- Runs the Maven TestNG suite
- Uploads `test-output/` as the `selenium-test-output` artifact
- Stops Selenium Grid after execution
