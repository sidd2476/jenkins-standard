def call() {  
  pipeline {
    agent any
    stages{
    stage('Verify Workspace'){
      steps{
      script{
      sh """
      git clone $GIT_URL
      echo "${env.JENKINS_HOME}"
      Map pipelineConfig = readYaml(file: "${WORKSPACE}/Jenkinsfile.yaml")
      """
      }
    }
    }
    stage('Build'){
      steps{
        script{
      echo "Building: ${pipelineConfig.project_name}"
        }
      }
    }
  }
  }
}  
