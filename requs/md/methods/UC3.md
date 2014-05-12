The following table describes all technical details of Use Case `UC3`:

<table>
      <thead>
         <tr>
            <th>Property</th>
            <th>Details</th>
         </tr>
      </thead>
      <tbody>
         <tr>
            <td>ID</td>
            <td>UC3</td>
         </tr>
         <tr>
            <td>Signature</td>
            <td>
               <code>_self</code> deploys itself</td>
         </tr>
         <tr>
            <td>Primary</td>
            <td>Repository</td>
         </tr>
         <tr>
            <td>Actors</td>
            <td/>
         </tr>
         <tr>
            <td>Brief</td>
            <td>"We fetch the repository every five minutes and check the difference between existing source code and the latest version. If there are any changes, we stop its Docker container, and start a new one with the latest version of the source code. Besides Dockerfile, which is used for the Docker container, we should look for a Manifest of the repository. According to the informaiton in the Manifest we should configure the container after its start. While the repository is used, we should track its CPU and traffic consumption, and assign to its owner"</td>
         </tr>
      </tbody>
   </table>

Actors taking participation in the Use Case have the following properties:

<table>
      <thead>
         <tr>
            <th>Actor</th>
            <th>Properties</th>
         </tr>
      </thead>
      <tbody>
         <tr>
            <td>Repository</td>
            <td>"a local copy of files and folders being pulled every 5 minutes from some remote source"<ul>
                  <li>
                     <code>name</code>:  "a unique name of the repo in user's account"</li>
                  <li>
                     <code>deployment</code>: Deployment</li>
                  <li>
                     <code>dockerFile</code>:  "Docker file"</li>
                  <li>
                     <code>manifest</code>: Manifest</li>
               </ul>
            </td>
         </tr>
      </tbody>
   </table>
