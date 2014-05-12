The following table describes all technical details of Use Case `UC4`:

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
            <td>UC4</td>
         </tr>
         <tr>
            <td>Signature</td>
            <td>
               <code>_self</code> reads deployment logs using <code>_arg0</code>
            </td>
         </tr>
         <tr>
            <td>Primary</td>
            <td>User</td>
         </tr>
         <tr>
            <td>Actors</td>
            <td>
               <code>_arg0</code> as Repository</td>
         </tr>
         <tr>
            <td>Success Flow</td>
            <td>1. <code>user</code> "requests a list of deployments for the Repository"<br/>2. <code>system</code> "displays a list of deployments that has already been executed"<br/>3. <code>user</code> "chooses a deployment and initiates viewing its log"<br/>4. <code>system</code> "displays a log for the chosen deployment"</td>
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
            <td>User</td>
            <td>"a person identified in the system (logged in)"<ul>
                  <li>
                     <code>repo</code>: Repository</li>
                  <li>
                     <code>URN</code>:  "a unique identifier of itself, for example urn:github:526301"</li>
                  <li>
                     <code>authTokens</code>:  "a list of auth tokens to each auth provider the user connected to, e.g., Goole, Facebook, etc."</li>
                  <li>
                     <code>balance</code>:  "amount of money available, can be stored as integer number of cents"</li>
               </ul>
            </td>
         </tr>
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
