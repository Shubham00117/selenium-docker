pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser to run the suite against')
        string(name: 'SUITE_XML', defaultValue: 'src/test/resources/test-suites/master.xml', description: 'TestNG suite XML to execute')
    }

    environment {
        SE_HUB_PUBLISH_PORT = '5542'
        SE_HUB_SUBSCRIBE_PORT = '5543'
        SE_HUB_UI_PORT = '5544'
        GRID_URL = 'http://localhost:5544'
        TZ = 'Asia/Kolkata'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Shubham00117/selenium-docker.git'
            }
        }

        stage('Start Selenium Grid') {
            steps {
                sh 'docker compose up -d --remove-orphans'
            }
        }

        stage('Wait for Grid') {
            steps {
                sh '''
                    for i in $(seq 1 30); do
                      if curl -fsS "$GRID_URL/status" | grep -Eq '"ready"[[:space:]]*:[[:space:]]*true'; then
                        echo "Selenium Grid is ready"
                        exit 0
                      fi

                      echo "Waiting for Selenium Grid..."
                      sleep 2
                    done

                    docker compose logs --tail=200 || true
                    exit 1
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh '''
                    BROWSER_VALUE="${BROWSER:-chrome}"
                    SUITE_XML_VALUE="${SUITE_XML:-src/test/resources/test-suites/master.xml}"

                    mvn -B -ntp test \
                      -Dexecution=grid \
                      -Dbrowser="$BROWSER_VALUE" \
                      -Dgrid.url="$GRID_URL" \
                      -Dsurefire.suiteXmlFiles="$SUITE_XML_VALUE" \
                      -Dtimezone="$TZ" \
                      -Dtest.timezone="$TZ"
                '''
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'test-output/**', allowEmptyArchive: true
            sh 'docker compose down -v --remove-orphans || true'
        }
        failure {
            sh 'docker compose logs --tail=200 || true'
        }
    }
}
