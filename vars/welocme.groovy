def call() { 
  Map pipelineConfig = [:]
  pipeline {
    agent any
    stages{
    stage('Verify Workspace'){
      steps{
      script{      
      pipelineConfig = readYaml(file: "Jenkinsfile.yaml")
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
    stage('Generate-tag'){
      steps{
        script{
          env.GIT_COMMIT_MSG = sh (script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
          sh '''
          git clone https://sidd2476:${pat}@github.com/helm-test.git
          cd helm-test
          wget https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64 -O /usr/bin/yq &&\
    chmod +x /usr/bin/yq
          yq '.global.saas' imageVersion.yaml
          /bin/bash
          pwd
          echo $GIT_COMMIT_MSG
          if [[ $GIT_COMMIT_MSG == *"bump_minor"* ]]; then
            export CI_TAG_INCRMENT_KEY="preminor"
          elif [[ $GIT_COMMIT_MSG == *"bump_major"* ]]; then
            export CI_TAG_INCRMENT_KEY="premajor"
          elif [[ $GIT_COMMIT_MSG == *"bump_patch"* ]]; then
            export CI_TAG_INCRMENT_KEY="prepatch"
          else
            export CI_TAG_INCRMENT_KEY="prerelease"
          fi
          
          export INITTAG=$(git describe --abbrev=0 --tags)||true
          export INITTAGSEMVER=$(npx semver INITTAG)
          export INITVERSION=$(npx semver ${INITTAGSEMVER:-"0.0.0"})
          export fullTag=$(git for-each-ref refs/tags --sort=-creatordate --format='%(refname)'|grep feature |head -1 )||true
          export GIT_TAG_FEATURE=${fullTag##*/}||true
          export GIT_TAG_FEATURE_SEMVER=$(npx semver -i ${GIT_TAG_FEATURE:-$INITVERSION})
          echo "GIT_TAG_FEATURE=$GIT_TAG_FEATURE GIT_TAG_FEATURE_SEMVER=$GIT_TAG_FEATURE_SEMVER"
    
          export CURRENT_TAG=$GIT_TAG_FEATURE
          echo "Setting GIT_TAG_FEATURE=$GIT_TAG_FEATURE"
    
          export CURRENT_TAG=${CURRENT_TAG:-${SEMVER_PREFIX}$INITVERSION}
          echo "export CURRENT_TAG=$CURRENT_TAG"
          echo "export CI_TAG_INCRMENT_KEY=$CI_TAG_INCRMENT_KEY"
          echo "export CI_TAG_KEYWORD=$CI_TAG_KEYWORD"
    
          export fullPrdTag=$(git for-each-ref refs/tags --sort=-creatordate --format='%(refname)' |grep -v '-'|head -1 )||true
          export PRD_TAG=${fullPrdTag##*/}||true
          echo "PRD_TAG=$PRD_TAG"
          export PRD_TAG_SEMVER=$(semver ${PRD_TAG:-"0.0.0"})
          export PRD_TAG_SEMVER=$(semver ${PRD_TAG_SEMVER:-"0.0.0"})
          echo "PRD_TAG_SEMVER=$PRD_TAG_SEMVER"
          echo "CURRENT_TAG=$CURRENT_TAG"
          export CURRENT_TAG_SEMVER=$(npx semver -i ${CURRENT_TAG:-$INITVERSION})
          echo "CURRENT_TAG_SEMVER=$CURRENT_TAG_SEMVER"
          PRD_VS_CURR=$(npx pysemver compare $PRD_TAG_SEMVER $CURRENT_TAG_SEMVER  2>&1)
          echo "PRD_VS_CURR=$PRD_VS_CURR"
    
          if (( $PRD_VS_CURR > -1 )); then
            export CURRENT_TAG=$PRD_TAG
            echo "Setting CURRENT_TAG to PRD_TAG=$PRD_TAG"
          else
            export CURRENT_TAG=$CURRENT_TAG
            echo "Setting CURRENT_TAG=$CURRENT_TAG"
          fi
          export TAG=${SEMVER_PREFIX:-v}$(npx semver $CURRENT_TAG -i ${CI_TAG_INCRMENT_KEY} --preid ${CI_TAG_KEYWORD:-feature})
          git tag -a $TAG -m "version $TAG"
          git push --tags ${GIT_URL}
          '''
        }
      }
    }
  }
  }
}  
