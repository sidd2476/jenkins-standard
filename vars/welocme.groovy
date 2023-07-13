def call() {  
  pipeline {
    agent any
    stages{
    stage('Verify Workspace'){
      script{
      sh """
      git clone $GIT_URL
      echo "${env.JENKINS_HOME}"
      Map pipelineConfig = readYaml(file: "${WORKSPACE}/Jenkinsfile.yaml")
      """
      }
    }
    stage('Build'){
      println "Building: ${pipelineConfig.project_name}"
    }
  }
  }
}  
