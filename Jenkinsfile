pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
    }

    /*
     * GitHub → Jenkins integration (one-time setup):
     *   1. Install "GitHub" plugin in Jenkins (Manage Jenkins > Plugins).
     *   2. In GitHub repo: Settings > Webhooks > Add webhook
     *        Payload URL : http://<JENKINS_URL>/github-webhook/
     *        Content type: application/json
     *        Events      : Just the push event
     *   3. In Jenkins job: Build Triggers > enable "GitHub hook trigger for GITScm polling".
     *      The `githubPush()` trigger below handles this declaratively.
     */
    triggers {
        githubPush()
    }

    parameters {
        choice(
            name: 'SUITE',
            choices: ['smoke', 'regression', 'all'],
            description: 'Test suite to run:\n  smoke      → only @smoke scenarios\n  regression → only @regression scenarios\n  all        → every scenario (no tag filter)'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox'],
            description: 'Browser for UI tests'
        )
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Validate Environment') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('UI Tests') {
            steps {
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    script {
                        def tagArg = params.SUITE == 'all' ? '' : "-Dcucumber.filter.tags=@${params.SUITE}"
                        bat "mvn clean test -Dtest=UiTestRunner -Dbrowser=${params.BROWSER} ${tagArg}"
                    }
                }
            }
        }

        stage('API Tests') {
            steps {
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    script {
                        def tagArg = params.SUITE == 'all' ? '' : "-Dcucumber.filter.tags=@${params.SUITE}"
                        bat "mvn clean test -Dtest=ApiTestRunner ${tagArg}"
                    }
                }
            }
        }

    }

    post {
        always {
            echo "Suite: ${params.SUITE} | Browser: ${params.BROWSER}"
            archiveArtifacts artifacts: 'reports/**, screenshots/**', allowEmptyArchive: true
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
        success {
            echo 'Pipeline finished successfully.'
        }
        unstable {
            echo 'Pipeline finished with unstable status. Review reports and screenshots.'
        }
        failure {
            echo 'Pipeline failed. Review reports and screenshots.'
        }
    }
}
