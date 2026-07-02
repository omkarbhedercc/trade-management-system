import React, { useState } from "react";

// Static snapshot of the intentionally-outdated stack and the CVEs / issues each
// dependency demonstrates. Kept in sync with backend/pom.xml and frontend/package.json.
const STACK = [
  {
    tier: "Frontend",
    items: [
      { name: "react / react-dom", version: "16.13.1", notes: [
        { sev: "info", text: "Old major — upgrade target React 18" },
      ]},
      { name: "react-scripts", version: "3.4.1", notes: [
        { sev: "high", text: "Pulls a large tree of vulnerable transitive deps" },
      ]},
      { name: "axios", version: "0.19.2", notes: [
        { sev: "high", text: "SSRF — CVE-2020-28168" },
        { sev: "medium", text: "ReDoS — CVE-2021-3749" },
      ]},
      { name: "lodash", version: "4.17.11", notes: [
        { sev: "high", text: "Prototype pollution — CVE-2019-10744" },
      ]},
      { name: "react-router-dom", version: "5.2.0", notes: [
        { sev: "info", text: "Old routing API" },
      ]},
    ],
  },
  {
    tier: "Backend",
    items: [
      { name: "Java (JDK)", version: "8", notes: [
        { sev: "info", text: "EOL-era runtime — upgrade to 17 / 21" },
      ]},
      { name: "Spring Boot", version: "2.3.4.RELEASE", notes: [
        { sev: "info", text: "EOL — upgrade to Spring Boot 3.x" },
      ]},
      { name: "log4j-core", version: "2.14.1", notes: [
        { sev: "critical", text: "Log4Shell RCE — CVE-2021-44228" },
        { sev: "critical", text: "Follow-up — CVE-2021-45046" },
      ]},
      { name: "jackson-databind", version: "2.9.10.4", notes: [
        { sev: "high", text: "Multiple deserialization RCE CVEs" },
      ]},
      { name: "postgresql (JDBC)", version: "42.2.5", notes: [
        { sev: "medium", text: "Known driver CVEs (e.g. CVE-2022-21724)" },
      ]},
      { name: "snakeyaml", version: "1.27", notes: [
        { sev: "high", text: "RCE on load — CVE-2022-1471" },
        { sev: "medium", text: "DoS — CVE-2022-25857" },
      ]},
      { name: "commons-collections", version: "3.2.1", notes: [
        { sev: "high", text: "Deserialization gadget — CVE-2015-7501" },
      ]},
    ],
  },
  {
    tier: "Database",
    items: [
      { name: "PostgreSQL", version: "15 (container)", notes: [
        { sev: "info", text: "Engine current — demo value is in the app deps" },
      ]},
    ],
  },
];

export default function DemoInfoButton() {
  const [open, setOpen] = useState(false);

  return (
    <div className="demo-widget">
      {open && (
        <div className="demo-card">
          <div className="demo-card-head">
            <span>Stack &amp; Known CVEs</span>
            <button className="demo-close" onClick={() => setOpen(false)} title="Close">×</button>
          </div>
          <div className="demo-card-body">
            {STACK.map((group) => (
              <div key={group.tier} className="demo-group">
                <div className="demo-tier">{group.tier}</div>
                {group.items.map((it) => (
                  <div key={it.name} className="demo-row">
                    <div className="demo-dep">
                      <span className="demo-name">{it.name}</span>
                      <span className="demo-ver">{it.version}</span>
                    </div>
                    <ul className="demo-notes">
                      {it.notes.map((n, i) => (
                        <li key={i}>
                          <span className={"demo-sev sev-" + n.sev}>{n.sev}</span>
                          <span className="demo-note-text">{n.text}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                ))}
              </div>
            ))}
            <div className="demo-foot">
              Intentionally outdated — the ModernizeX scan payload. Do not upgrade.
            </div>
          </div>
        </div>
      )}
      <button
        className="demo-fab"
        onClick={() => setOpen((o) => !o)}
        title="Demo: stack versions &amp; CVEs"
      >
        {open ? "×" : "!"}
      </button>
    </div>
  );
}
