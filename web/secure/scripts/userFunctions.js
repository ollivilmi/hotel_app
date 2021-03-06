const getJobOptions = (department) => {
    
    return fetch("/management/r/jobs/byDepartment?dptId="+department)
          .then(response => response.json())
          .then(function(json) 
    {
        let jobSelect = "";
        
        for(let job of json)
            jobSelect += '<option value="' + job.id + '">' + job.title + '</option>';
        
        return jobSelect;
    })
            .catch(error => console.log(error));  
};

const jobOptions = (element, value) =>
{
  element.innerHTML = jobs[value];  
};

const getManagementOptions = () => {
    return  '<option value="1">Restaurant</option>'
            + '<option value="2">Management</option>'
            + '<option value="3">Reception</option>'
            + '<option value="4">Maintenance</option>';
};

const getCookie = (cname) => {
    const name = cname + "=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
};