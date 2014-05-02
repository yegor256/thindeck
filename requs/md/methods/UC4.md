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
            <td>Brief</td>
            <td>"The user clicks his repository and opens a page with a list of latest deployments. Every Deployment can be viewed as plain text in a browser, after Deployment has been executed. System will not display logs in real-time (live)"</td>
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
               </ul>
            </td>
         </tr>
         <tr>
            <td>Repository</td>
            <td>"a storage of source code together with Dockerfile"<ul>
                  <li>
                     <code>name</code>:  "a unique name of the repo in user's account"</li>
                  <li>
                     <code>URI</code>:  "a non-ambiguous descriptor of a repo, for example ssh://git@github:yegor256/thindeck.git"</li>
                  <li>
                     <code>key</code>:  "a private SSH key, see http://en.wikipedia.org/wiki/Public-key_cryptography"</li>
                  <li>
                     <code>deployment</code>: Deployment</li>
               </ul>
            </td>
         </tr>
      </tbody>
   </table>
